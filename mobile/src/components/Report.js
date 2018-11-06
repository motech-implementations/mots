import React, { Component } from 'react';
import { View, ScrollView, Text } from 'react-native';
import PropTypes from 'prop-types';
import { Actions } from 'react-native-router-flux';
import { Table, Row } from 'react-native-table-component';
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
const fontWidth = 8;
const rowHeight = 24;

export default class Report extends Component {
  static getTableData(values) {
    const tableData = [];
    values.forEach((row) => {
      const rowData = [];
      Object.keys(row).forEach((colName) => {
        rowData.push(row[colName]);
      });
      tableData.push(rowData);
    });
    return tableData;
  }

  static getTableHeaders(colModel) {
    const columns = [];
    Object.keys(colModel).forEach((colName) => {
      columns.push(colModel[colName][0]);
    });
    columns.sort((a, b) => a.order - b.order);
    return columns.map(column => column.Header);
  }

  static getColumnWidths(headers, tableData) {
    const widths = headers.map(header => Math.max(50, (header.length * fontWidth)));
    tableData.forEach((rowData) => {
      rowData.forEach((value, i) => {
        const len = (value.length * fontWidth);
        if (len > widths[i]) {
          widths[i] = len;
        }
      });
    });
    return widths;
  }

  constructor(props) {
    super(props);
    this.state = {
      reportId: '',
      reportName: '',
      templateParameters: [],
      currentPage: 1,
      totalPages: 1,
      pageSize: null,
      maxPageSize: 2147483647,
      staticParams: ['pageSize', 'offset', 'orderBy'],
      tableHeaders: [],
      columnWidths: [],
      tableData: [],
    };

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
        this.setState({
          reportName: this.props.reportName,
          templateParameters: this.props.templateParameters,
        });
        this.setState({ reportId: this.props.reportId }, () => {
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

  fetchReport = (pageNumber, size) => {
    const pageSize = size || this.state.pageSize;
    if (!pageSize) return;
    this.setState({
      pageSize,
    });
    const offset = (pageNumber - 1) * pageSize;
    const params = this.getParams(pageSize);
    const url = `/api/reports/templates/${this.state.reportId}/json?${params}&offset=${offset}`;

    apiClient.get(url)
      .then((response) => {
        const { colModel, totalPages, values } = response[0];

        const templateParameters = [];
        Object.keys(colModel[0]).forEach((colName) => {
          const { order } = colModel[0][colName][0];
          const parameter = this.state.templateParameters.find(param =>
            param.name === colName);
          if (parameter) {
            templateParameters.push({
              ...parameter,
              order: parseInt(order, 10),
            });
          }
        });
        const tableHeaders = Report.getTableHeaders(colModel[0]);
        const tableData = Report.getTableData(values);
        const columnWidths = Report.getColumnWidths(tableHeaders, tableData);

        this.setState({
          tableHeaders,
          tableData,
          columnWidths,
          templateParameters: templateParameters.sort((a, b) => a.order - b.order),
          totalPages,
        });
      });
  };

  render() {
    const { tableHeaders, columnWidths, tableData } = this.state;
    return (
      <View style={getContainerStyle()}>
        <Text style={[styles.formHeader, lightThemeText]}>{this.state.reportName}</Text>

        <View style={reportStyles.buttonContainer}>
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
        <ScrollView horizontal>
          <View style={reportStyles.container}>
            <Table borderStyle={{ borderColor: '#c1c0b9' }}>
              <Row
                data={tableHeaders}
                widthArr={columnWidths}
                style={reportStyles.header}
                textStyle={reportStyles.text}
              />
            </Table>
            <ScrollView
              style={reportStyles.dataWrapper}
              onLayout={(event) => {
              const { height } = event.nativeEvent.layout;
              if (!this.state.pageSize) {
                this.fetchReport(1, Math.floor(height / rowHeight));
              }
            }}
            >
              <Table borderStyle={{ borderColor: '#c1c0b9' }}>
                {
                  tableData.map((rowData, index) => (
                    <Row
                      key={index}
                      data={rowData}
                      widthArr={columnWidths}
                      style={[reportStyles.row, { height: rowHeight }, index % 2 && { backgroundColor: '#fff' }]}
                      textStyle={reportStyles.text}
                    />
                  ))
                }
              </Table>
            </ScrollView>
          </View>
        </ScrollView>

        <View style={[reportStyles.buttonContainer, { paddingVertical: 5, alignItems: 'center' }]}>
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
