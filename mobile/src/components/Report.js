import React, { Component } from 'react';
import { connect } from 'react-redux';
import { View, ScrollView, Text } from 'react-native';
import PropTypes from 'prop-types';
import { Actions } from 'react-native-router-flux';
import { Table, Row } from 'react-native-table-component';
import { fetchReport } from '../actions/index';
import apiClient from '../utils/api-client';

import {
  DISPLAY_REPORTS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';
import Button from './Button';
import styles from '../styles/formsStyles';
import reportStyles from '../styles/reportViewStyles';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';

const { lightThemeText } = commonStyles;
const fontWidth = 8;
const rowHeight = 24;

class Report extends Component {
  static getTableRows(columns, values) {
    const tableRows = [];
    values.forEach((row) => {
      const rowData = [];
      Object.keys(row).forEach((colName) => {
        rowData.push(row[colName]);
      });
      tableRows.push(rowData);
    });
    return tableRows;
  }

  static getTableColumns(colModel) {
    const columns = [];
    Object.keys(colModel).forEach((colName) => {
      const column = colModel[colName][0];
      columns.push({
        order: column.order,
        label: column.Header,
        key: colName,
      });
    });
    return columns.sort((a, b) => a.order - b.order);
  }

  static getColumnWidths(headers, tableRows) {
    const widths = headers.map(header => Math.max(50, (header.length * fontWidth)));
    tableRows.forEach((rowData) => {
      rowData.forEach((value, i) => {
        const len = (value.length * fontWidth);
        if (len > widths[i]) {
          widths[i] = len;
        }
      });
    });
    return widths;
  }

  static INITIAL_STATE = {
    staticParams: ['pageSize', 'offset', 'orderBy'],
    tableHeaders: [],
    columnWidths: [],
    tableRows: [],
    reportJson: null,
    pageSize: 10,
    currentPage: 1,
    totalPages: 1,
    totalValues: 0,
  };

  constructor(props) {
    super(props);
    this.state = Report.INITIAL_STATE;

    this.fetchNextPage = this.fetchNextPage.bind(this);
    this.fetchPrevPage = this.fetchPrevPage.bind(this);
    this.fetchFirstPage = this.fetchFirstPage.bind(this);
    this.fetchLastPage = this.fetchLastPage.bind(this);
    this.fetchPdf = this.fetchPdf.bind(this);
    this.fetchXls = this.fetchXls.bind(this);
    this.setTableData = this.setTableData.bind(this);
  }

  componentDidMount() {
    hasAuthority(DISPLAY_REPORTS_AUTHORITY).then((result) => {
      if (!result) {
        Actions.home();
      } else {
        this.setTableData();
        this.props.fetchReport(this.props.reportId);
      }
    });
  }

  componentDidUpdate(prevProps) {
    if (this.props.reportId !== prevProps.reportId) {
      this.resetState();
      this.props.fetchReport(this.props.reportId);
    }
    if (this.state.reportJson !== this.props.reports[this.props.reportId]) {
      this.setTableData();
    }
  }

  getParams(pageSize) {
    let stringParams = `pageSize=${pageSize}`;
    const params = this.props.templateParameters.map(param => param);
    params.forEach((param) => {
      if (param.defaultValue && !this.state.staticParams.includes(param.name)) {
        stringParams += `&${param.name}=${param.defaultValue}`;
      }
    });
    return stringParams;
  }

  setPageSize(newPageSize) {
    const { pageSize, currentPage, totalValues } = this.state;
    if (pageSize !== newPageSize) {
      const currentIndex = pageSize * (currentPage - 1);
      this.setState({
        pageSize: newPageSize,
        currentPage: Math.floor(currentIndex / newPageSize) + 1,
        totalPages: Math.ceil(totalValues / newPageSize),
      }, () => {
        this.setTableData();
      });
    }
  }

  setTableData() {
    const reportJson = this.props.reports[this.props.reportId];
    if (reportJson && reportJson.length) {
      const { colModel, values } = reportJson[0];
      // get data for current page
      const { pageSize, currentPage } = this.state;
      const totalValues = values.length;
      const totalPages = Math.ceil(totalValues / pageSize);
      const currentValues = values.slice((currentPage - 1) * pageSize, currentPage * pageSize);
      // prepare arrays for table-component: headers, rows and column widths
      const columns = Report.getTableColumns(colModel[0]);
      const tableHeaders = columns.map(column => column.label);
      const tableRows = Report.getTableRows(columns, currentValues);
      const columnWidths = Report.getColumnWidths(tableHeaders, tableRows);
      this.setState({
        tableHeaders,
        tableRows,
        columnWidths,
        totalPages,
        totalValues,
        reportJson,
      });
    }
  }

  resetState() {
    this.setState(Report.INITIAL_STATE);
  }

  fetchNextPage() {
    const { currentPage } = this.state;
    if (currentPage + 1 <= this.state.totalPages) {
      this.setState({ currentPage: currentPage + 1 }, () => {
        this.setTableData();
      });
    }
  }

  fetchPrevPage() {
    const { currentPage } = this.state;
    if (currentPage - 1 >= 1) {
      this.setState({ currentPage: currentPage - 1 }, () => {
        this.setTableData();
      });
    }
  }

  fetchFirstPage() {
    const { currentPage } = this.state;
    if (currentPage !== 1) {
      this.setState({ currentPage: 1 }, () => {
        this.setTableData();
      });
    }
  }

  fetchLastPage() {
    const { currentPage } = this.state;
    if (currentPage !== this.state.totalPages) {
      this.setState({ currentPage: this.state.totalPages }, () => {
        this.setTableData();
      });
    }
  }

  fetchPdf() {
    const url = `/api/reports/templates/${this.props.reportId}/pdf?pageSize=${this.state.maxPageSize}`;
    apiClient.downloadReport(url, this.props.reportName, 'pdf');
  }

  fetchXls() {
    const url = `/api/reports/templates/${this.props.reportId}/xls?pageSize=${this.state.maxPageSize}`;
    apiClient.downloadReport(url, this.props.reportName, 'xls');
  }

  prevDisabled() {
    return this.state.currentPage === 1;
  }

  nextDisabled() {
    return this.state.currentPage === this.state.totalPages;
  }

  render() {
    const { tableHeaders, columnWidths, tableRows } = this.state;
    return (
      <View style={getContainerStyle()}>
        <Text style={[styles.formHeader, lightThemeText]}>{this.props.reportName}</Text>

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
              this.setPageSize(Math.floor(height / rowHeight));
            }}
            >
              <Table borderStyle={{ borderColor: '#c1c0b9' }}>
                {
                  tableRows.map((rowData, index) => (
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
      </View>
    );
  }
}

function mapStateToProps(state) {
  return {
    reports: state.reportReducer,
  };
}

export default connect(mapStateToProps, { fetchReport })(Report);

Report.propTypes = {
  reports: PropTypes.shape({}).isRequired,
  fetchReport: PropTypes.func.isRequired,
  reportName: PropTypes.string.isRequired,
  reportId: PropTypes.string.isRequired,
  templateParameters: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
};
