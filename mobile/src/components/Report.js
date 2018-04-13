import React, { Component } from 'react';
import { View, Text, WebView } from 'react-native';
import PropTypes from 'prop-types';
import { Actions } from 'react-native-router-flux';
import apiClient from '../utils/api-client';

import {
  DISPLAY_REPORTS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';
import Button from './Button';
import Filters from './Filters';
import styles from '../styles/formsStyles';
import reportStyles from '../styles/reportViewStyles';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';

const { lightThemeText } = commonStyles;

export default class Report extends Component {
  constructor(props) {
    super(props);
    this.state = {
      reportHtml: '',
      reportId: '',
      reportName: '',
      templateParameters: [],
      currentPage: 1,
      totalPages: 1,
      pageSize: 20,
      maxPageSize: 2147483647,
      staticParams: ['pageSize', 'offset', 'orderBy'],
    };

    this.checkPageCount = this.checkPageCount.bind(this);
    this.fetchReport = this.fetchReport.bind(this);
    this.fetchNextPage = this.fetchNextPage.bind(this);
    this.fetchPrevPage = this.fetchPrevPage.bind(this);
    this.fetchFirstPage = this.fetchFirstPage.bind(this);
    this.fetchLastPage = this.fetchLastPage.bind(this);
    this.fetchPdf = this.fetchPdf.bind(this);
    this.fetchXls = this.fetchXls.bind(this);
  }

  componentWillMount() {
    hasAuthority(DISPLAY_REPORTS_AUTHORITY).then((result) => {
      if (result) {
        const size = this.props.reportName.includes('CHW List') ||
          this.props.reportName.includes('Selected CHWs') ? 10 : 20;
        this.setState({
          reportName: this.props.reportName,
          templateParameters: this.props.templateParameters,
          pageSize: size,
        });
        this.setState({ reportId: this.props.reportId }, () => {
          this.checkPageCount();
          this.fetchReport(1);
        });
      } else {
        Actions.home();
      }
    });
  }

  componentWillReceiveProps(nextProps) {
    this.setState({ reportId: nextProps.reportId }, () => this.fetchReport(1));
  }

  onFilter(filters) {
    this.setState({
      templateParameters: filters,
      currentPage: 1,
    }, () => this.fetchReport(1));
  }

  onReset() {
    this.setState({
      templateParameters: this.state.templateParameters.map((param) => {
        if (!this.state.staticParams.includes(param.name)) {
          return {
            ...param,
            defaultValue: null,
          };
        }
        return param;
      }),
      currentPage: 1,
    }, () => this.fetchReport(1));
  }

  getParams(pageSize) {
    let stringParams = `pageSize=${pageSize}`;
    const params = this.state.templateParameters.map(param => param);
    params.forEach((param) => {
      if (param.defaultValue && !this.state.staticParams.includes(param.name)) {
        stringParams += `&${param.name}=${param.defaultValue}`;
      }
    });
    return stringParams;
  }

  checkPageCount() {
    const params = this.getParams(1);
    const url = `/api/reports/templates/${this.state.reportId}/json?${params}`;

    apiClient.get(url)
      .then((response) => {
        if (response) {
          this.setState({
            totalPages: Math.ceil(response[0].totalPages / this.state.pageSize) || 1,
          });
        }
      });
  }

  fetchReport = (pageNumber) => {
    this.setState({ reportHtml: '' });
    const offset = (pageNumber - 1) * this.state.pageSize;
    const params = this.getParams(this.state.pageSize);
    const url = `/api/reports/templates/${this.state.reportId}/html?${params}&offset=${offset}`;

    apiClient.getText(url)
      .then((response) => {
        this.setState({
          reportHtml: response,
        });
        this.checkPageCount();
      });
  };

  fetchNextPage() {
    const { currentPage } = this.state;
    if (currentPage + 1 <= this.state.totalPages) {
      this.fetchReport(currentPage + 1);
      this.setState({ currentPage: currentPage + 1 });
    }
  }

  fetchPrevPage() {
    const { currentPage } = this.state;
    if (currentPage - 1 >= 1) {
      this.fetchReport(currentPage - 1);
      this.setState({ currentPage: currentPage - 1 });
    }
  }

  fetchFirstPage() {
    const { currentPage } = this.state;
    if (currentPage !== 1) {
      this.fetchReport(1);
      this.setState({ currentPage: 1 });
    }
  }

  fetchLastPage() {
    const { currentPage } = this.state;
    if (currentPage !== this.state.totalPages) {
      this.fetchReport(this.state.totalPages);
      this.setState({ currentPage: this.state.totalPages });
    }
  }

  fetchPdf() {
    const url = `/api/reports/templates/${this.state.reportId}/pdf?pageSize=${this.state.maxPageSize}`;
    apiClient.downloadReport(url, this.state.reportName, 'pdf');
  }

  fetchXls() {
    const url = `/api/reports/templates/${this.state.reportId}/xls?pageSize=${this.state.maxPageSize}`;
    apiClient.downloadReport(url, this.state.reportName, 'xls');
  }

  prevDisabled() {
    return this.state.currentPage === 1;
  }

  nextDisabled() {
    return this.state.currentPage === this.state.totalPages;
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <Text style={[styles.formHeader, lightThemeText]}>{this.state.reportName}</Text>

        <View style={styles.buttonContainer}>
          <Button
            onPress={() => this.fetchPdf()}
            iconName="download"
            iconColor="#FFF"
            buttonColor="#337ab7"
          >
                      Download PDF
          </Button>
          <Button
            onPress={() => this.fetchXls()}
            iconName="download"
            iconColor="#FFF"
            buttonColor="#449C44"
            marginLeft={10}
          >
                      Download XLS
          </Button>
        </View>
        <View style={reportStyles.reportHtmlStyle}>
          <WebView
            source={{ html: this.state.reportHtml }}
            style={{ flex: 1 }}
          />
        </View>

        <View style={[styles.buttonContainer, { paddingVertical: 10, alignItems: 'center' }]}>
          <Button
            onPress={() => this.fetchFirstPage()}
            iconName="angle-double-left"
            iconColor="#FFF"
            buttonColor={this.prevDisabled() ? '#c3c3c3' : '#337ab7'}
            disabled={this.prevDisabled()}
          />
          <Button
            onPress={() => this.fetchPrevPage()}
            iconName="angle-left"
            iconColor="#FFF"
            buttonColor={this.prevDisabled() ? '#c3c3c3' : '#337ab7'}
            disabled={this.prevDisabled()}
            marginLeft={5}
          />
          <Text style={{ marginHorizontal: 10 }}>
            {`${this.state.currentPage} / ${this.state.totalPages}`}
          </Text>
          <Button
            onPress={() => this.fetchNextPage()}
            iconName="angle-right"
            iconColor="#FFF"
            buttonColor={this.nextDisabled() ? '#c3c3c3' : '#337ab7'}
            disabled={this.nextDisabled()}
          />
          <Button
            onPress={() => this.fetchLastPage()}
            iconName="angle-double-right"
            iconColor="#FFF"
            buttonColor={this.nextDisabled() ? '#c3c3c3' : '#337ab7'}
            disabled={this.nextDisabled()}
            marginLeft={5}
          />
        </View>

        <Filters
          availableFilters={this.state.templateParameters}
          onFilter={filters => this.onFilter(filters)}
          onReset={() => this.onReset()}
          iconBottom={10}
          iconRight={20}
        />
      </View>
    );
  }
}

Report.propTypes = {
  reportName: PropTypes.string.isRequired,
  reportId: PropTypes.string.isRequired,
  templateParameters: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
};
