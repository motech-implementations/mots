import React, { Component } from 'react';
import { connect } from 'react-redux';
import { View, ScrollView, Text, TouchableOpacity } from 'react-native';
import PropTypes from 'prop-types';
import Icon from 'react-native-vector-icons/FontAwesome';
import { Actions } from 'react-native-router-flux';
import { Table, Row } from 'react-native-table-component';
import Filters, { STATIC_FILTERS } from './Filters';
import { fetchReport } from '../actions/index';
import apiClient from '../utils/api-client';
import createFilter from '../utils/filter';
import createSorter from '../utils/sort';

import {
  DISPLAY_REPORTS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';
import Button from './Button';
import reportStyles from '../styles/reportViewStyles';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';

const { lightThemeText } = commonStyles;
const fontWidth = 8;
const rowHeight = 22;
const sortIconSize = 16;

class Report extends Component {
  static getTableRows(values) {
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

  static getColumnWidths(columns, tableRows) {
    const widths = columns.map(column => Math.max(50, (
      column.label.length * fontWidth) + sortIconSize));
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

  static formatDate(date) {
    const dateAndTime = new Date(date).toISOString().split('T');
    const time = dateAndTime[1].split('.')[0];
    return `${dateAndTime[0]} ${time}`;
  }

  constructor(props) {
    super(props);
    this.state = this.getInitialState();

    this.nextPage = this.nextPage.bind(this);
    this.prevPage = this.prevPage.bind(this);
    this.firstPage = this.firstPage.bind(this);
    this.lastPage = this.lastPage.bind(this);
    this.fetchPdf = this.fetchPdf.bind(this);
    this.fetchXls = this.fetchXls.bind(this);
    this.setTableData = this.setTableData.bind(this);
  }

  getInitialState() {
    return {
      staticParams: ['pageSize', 'offset', 'orderBy'],
      tableColumns: [],
      tableHeaders: [],
      columnWidths: [],
      tableRows: [],
      reportJson: null,
      pageSize: 10,
      currentPage: 1,
      totalPages: 1,
      totalValues: 0,
      filters: [],
      sorters: [],
      templateParameters: this.props.templateParameters,
      syncDate: null,
      syncing: false,
      filtersVisible: false,
    };
  }


  componentDidMount() {
    hasAuthority(DISPLAY_REPORTS_AUTHORITY).then((result) => {
      if (!result) {
        Actions.home();
      } else {
        this.resetState();
        this.setTableData();
      }
    });
  }

  componentDidUpdate(prevProps) {
    if (this.props.reportId !== prevProps.reportId) {
      // switched to another report
      this.resetState();
    } else if (this.state.syncDate !== this.getCurrentReport().syncDate) {
      // syncing complete
      this.onSyncComplete();
    }
  }

  onFilter(templateParameters) {
    const filters = templateParameters
      .filter(param => param.defaultValue !== null && STATIC_FILTERS.indexOf(param.name) === -1)
      .map(param => ({
        property: param.name,
        value: param.defaultValue,
      }));
    this.setState({
      templateParameters,
      filters,
      currentPage: 1,
    }, () => this.setTableData());
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
      filters: [],
      currentPage: 1,
    }, () => this.setTableData());
  }

  onSyncComplete() {
    this.setState({
      syncing: false,
      syncDate: this.getCurrentReport().syncDate,
    });
    if (this.state.reportJson !== this.getCurrentReport().jsonData) {
      // the report has updated
      this.setTableData();
    }
  }

  setTableData() {
    const reportJson = this.getCurrentReport().jsonData;
    if (reportJson && reportJson.length) {
      const { colModel } = reportJson[0];
      const { filters, sorters } = this.state;
      let { values } = reportJson[0];
      // filter and sort values
      values = values.filter(createFilter(...filters));
      values.sort(createSorter(...sorters));
      // get data for current page
      const { pageSize, currentPage } = this.state;
      const totalValues = values.length;
      const totalPages = Math.ceil((totalValues || 1) / pageSize);
      const currentValues = values.slice((currentPage - 1) * pageSize, currentPage * pageSize);
      // prepare arrays for table-component: headers, rows and column widths
      const tableColumns = Report.getTableColumns(colModel[0]);
      const tableHeaders = tableColumns.map(column => this.getHeaderCell(column));
      const tableRows = Report.getTableRows(currentValues);
      const columnWidths = Report.getColumnWidths(tableColumns, tableRows);
      // sort template parameters with the same order as columns
      const templateParameters = this.getSortedTemplateParameters(tableColumns);
      this.setState({
        tableHeaders,
        tableRows,
        columnWidths,
        totalPages,
        totalValues,
        reportJson,
        templateParameters,
      });
    }
  }

  getHeaderCell(column) {
    const sorter = this.findSorter(column.key);
    let icon = 'sort';
    let color = '#ced4da';
    if (sorter) {
      color = '#495057';
      icon = (sorter.direction === 'asc') ? 'sort-up' : 'sort-down';
    }
    return (
      <TouchableOpacity onPress={() => this.sortBy(column.key)}>
        <View style={reportStyles.headerCell}>
          <Text style={reportStyles.text}>{column.label}</Text>
          <Icon style={reportStyles.icon} name={icon} size={sortIconSize} color={color} />
        </View>
      </TouchableOpacity>
    );
  }


  setPageSize(newPageSize) {
    const {
      pageSize, currentPage, totalValues, filtersVisible,
    } = this.state;
    if (pageSize !== newPageSize && !filtersVisible) {
      const currentIndex = pageSize * (currentPage - 1);
      this.setState({
        pageSize: newPageSize,
        currentPage: Math.floor(currentIndex / newPageSize) + 1,
        totalPages: Math.max(Math.ceil(totalValues / newPageSize), 1),
      }, () => {
        this.setTableData();
      });
    }
  }

  getCurrentReport() {
    return this.props.reports[this.props.reportId] || {};
  }

  onFilterVisibilityToggle(filtersVisible) {
    this.setState({
      filtersVisible,
    });
  }

  getSortedTemplateParameters(tableColumns) {
    const templateParameters = [];
    tableColumns.forEach((column) => {
      const parameter = this.state.templateParameters.find(param =>
        param.name === column.key);
      if (parameter) {
        templateParameters.push(parameter);
      }
    });
    return templateParameters;
  }

  findSorter(columnKey) {
    return this.state.sorters.find(s => s.property === columnKey);
  }

  sortBy(columnKey) {
    let sorter = this.findSorter(columnKey);
    if (sorter) {
      sorter.direction = (sorter.direction === 'asc') ? 'desc' : 'asc';
    } else {
      sorter = {
        property: columnKey,
        direction: 'asc',
      };
    }
    this.setState({
      currentPage: 1,
      sorters: [sorter],
    }, () => {
      this.setTableData();
    });
  }

  resetState() {
    this.setState(this.getInitialState());
  }

  nextPage() {
    const { currentPage } = this.state;
    if (currentPage + 1 <= this.state.totalPages) {
      this.setState({ currentPage: currentPage + 1 }, () => {
        this.setTableData();
      });
    }
  }

  prevPage() {
    const { currentPage } = this.state;
    if (currentPage - 1 >= 1) {
      this.setState({ currentPage: currentPage - 1 }, () => {
        this.setTableData();
      });
    }
  }

  firstPage() {
    const { currentPage } = this.state;
    if (currentPage !== 1) {
      this.setState({ currentPage: 1 }, () => {
        this.setTableData();
      });
    }
  }

  lastPage() {
    const { currentPage } = this.state;
    if (currentPage !== this.state.totalPages) {
      this.setState({ currentPage: this.state.totalPages }, () => {
        this.setTableData();
      });
    }
  }

  synchronize() {
    this.props.fetchReport(this.props.reportId);
    this.setState({
      syncing: true,
    });
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
        <View style={[reportStyles.pageHeader, lightThemeText]}>
          <Text style={[reportStyles.reportName,
            this.state.syncDate ? [] : reportStyles.additionalPadding]}
          >
            { this.props.reportName }
          </Text>
          {this.state.syncDate &&
          <Text style={reportStyles.syncDate}>
            Synchronized at {Report.formatDate(this.state.syncDate)}
          </Text>
          }
        </View>

        <View style={reportStyles.buttonContainer}>
          <Button
            onPress={() => this.synchronize()}
            iconName="refresh"
            iconColor="#FFF"
            buttonColor="#337ab7"
            disabled={this.state.syncing || !this.props.isConnected}
            spinning={this.state.syncing}
            style={reportStyles.synchronizeButton}
          >
            Synchronize
          </Button>
          <View style={reportStyles.downloadButtons}>
            <Button
              onPress={() => this.fetchPdf()}
              iconName="download"
              iconColor="#FFF"
              buttonColor="#337ab7"
              disabled={!this.props.isConnected}
            >
              PDF
            </Button>
            <Button
              onPress={() => this.fetchXls()}
              iconName="download"
              iconColor="#FFF"
              buttonColor="#449C44"
              style={{ marginLeft: 10 }}
              disabled={!this.props.isConnected}
            >
              XLS
            </Button>
          </View>
        </View>
        {!this.state.syncDate &&
        <Text style={{ margin: 10 }}>
          This report has not yet been synchronized.
        </Text>
        }
        <ScrollView horizontal>
          <View style={reportStyles.container}>
            <Table borderStyle={{ borderColor: '#c1c0b9' }}>
              <Row
                data={tableHeaders}
                widthArr={columnWidths}
                style={reportStyles.header}
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

        <View style={[reportStyles.buttonContainer, { paddingVertical: 5, alignItems: 'center', justifyContent: 'center' }]}>
          <Button
            onPress={() => this.firstPage()}
            iconName="angle-double-left"
            iconColor="#FFF"
            buttonColor={this.prevDisabled() ? '#c3c3c3' : '#337ab7'}
            disabled={this.prevDisabled()}
          />
          <Button
            onPress={() => this.prevPage()}
            iconName="angle-left"
            iconColor="#FFF"
            buttonColor={this.prevDisabled() ? '#c3c3c3' : '#337ab7'}
            disabled={this.prevDisabled()}
            style={{ marginLeft: 5 }}
          />
          <Text style={{ marginHorizontal: 10 }}>
            {`${this.state.currentPage} / ${this.state.totalPages}`}
          </Text>
          <Button
            onPress={() => this.nextPage()}
            iconName="angle-right"
            iconColor="#FFF"
            buttonColor={this.nextDisabled() ? '#c3c3c3' : '#337ab7'}
            disabled={this.nextDisabled()}
          />
          <Button
            onPress={() => this.lastPage()}
            iconName="angle-double-right"
            iconColor="#FFF"
            buttonColor={this.nextDisabled() ? '#c3c3c3' : '#337ab7'}
            disabled={this.nextDisabled()}
            style={{ marginLeft: 5 }}
          />
        </View>
        <Filters
          availableFilters={this.state.templateParameters}
          onFilter={filters => this.onFilter(filters)}
          onReset={() => this.onReset()}
          onVisibilityToggle={isVisible => this.onFilterVisibilityToggle(isVisible)}
          iconBottom={7}
          iconRight={20}
        />
      </View>
    );
  }
}

function mapStateToProps(state) {
  return {
    reports: state.reportReducer.reports,
    isConnected: state.connectionReducer.isConnected,
  };
}

export default connect(mapStateToProps, { fetchReport })(Report);

Report.propTypes = {
  reports: PropTypes.shape({}).isRequired,
  isConnected: PropTypes.bool.isRequired,
  fetchReport: PropTypes.func.isRequired,
  reportName: PropTypes.string.isRequired,
  reportId: PropTypes.string.isRequired,
  templateParameters: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
};
