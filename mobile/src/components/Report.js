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
import styles from '../styles/formsStyles';
import reportStyles from '../styles/reportViewStyles';
import getContainerStyle from '../utils/styleUtils';

export default class Report extends Component {
  constructor(props) {
    super(props);
    this.state = {
      reportHtml: '',
      reportId: '',
      reportName: '',
    };

    this.fetchReport = this.fetchReport.bind(this);
    this.fetchPdf = this.fetchPdf.bind(this);
    this.fetchXls = this.fetchXls.bind(this);
  }

  componentWillMount() {
    hasAuthority(DISPLAY_REPORTS_AUTHORITY).then((result) => {
      if (result) {
        this.setState({ reportName: this.props.reportName });
        this.setState({ reportId: this.props.reportId }, () => this.fetchReport());
      } else {
        Actions.home();
      }
    });
  }

  componentWillReceiveProps(nextProps) {
    this.setState({ reportId: nextProps.reportId }, () => this.fetchReport());
  }

    fetchReport = () => {
      const url = `/api/reports/templates/${this.state.reportId}/html`;

      apiClient.getText(url)
        .then((response) => {
          this.setState({ reportHtml: response });
        });
    };

    fetchPdf() {
      const url = `/api/reports/templates/${this.state.reportId}/pdf`;
      apiClient.downloadReport(url, this.state.reportName, 'pdf');
    }

    fetchXls() {
      const url = `/api/reports/templates/${this.state.reportId}/xls`;
      apiClient.downloadReport(url, this.state.reportName, 'xls');
    }

    render() {
      return (
        <View style={getContainerStyle()}>
          <Text style={styles.formHeader}>{this.state.reportName}</Text>

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
        </View>
      );
    }
}

Report.propTypes = {
  reportName: PropTypes.string.isRequired,
  reportId: PropTypes.string.isRequired,
};
