import React, { Component } from 'react';
import { FlatList, View, Text } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import ListItem from '../components/ListItem';
import Filters from '../components/Filters';
import { fetchChws } from '../actions/index';
import Button from '../components/Button';
import Spinner from '../components/Spinner';
import {
  CHW_WRITE_AUTHORITY, ASSIGN_MODULES_AUTHORITY,
  hasAuthority } from '../utils/authorization';
import styles from '../styles/listsStyles';
import commonStyles from '../styles/commonStyles';
import buildSearchParams from '../utils/search-params';

const { lightThemeText } = commonStyles;

class HealthWorkersList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      CHW_WRITE_AUTHORITY: false,
      ASSIGN_MODULES_AUTHORITY: false,
      templateParams: this.getColumnDefinitions(),
      loading: false,
      noDataMessage: 'No data found',
    };
  }

  componentWillMount() {
    hasAuthority(CHW_WRITE_AUTHORITY).then((result) => {
      if (result) { this.setState({ CHW_WRITE_AUTHORITY: true }); }
    });
    hasAuthority(ASSIGN_MODULES_AUTHORITY).then((result) => {
      if (result) { this.setState({ ASSIGN_MODULES_AUTHORITY: true }); }
    });
    this.fetchData();
  }

  onFilter(filters) {
    this.setState({
      templateParams: filters,
    }, () => this.fetchData());
  }

  onReset() {
    this.setState({
      templateParams: this.state.templateParams.map(param => ({
        ...param,
        defaultValue: null,
      })),
    }, () => this.fetchData());
  }

  getColumnDefinitions() {
    return [
      {
        displayName: 'ID',
        name: 'chwId',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'First name',
        name: 'firstName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Surname',
        name: 'secondName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Other name',
        name: 'otherName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'YOB',
        name: 'yearOfBirth',
      }, {
        displayName: 'Gender',
        name: 'gender',
      }, {
        displayName: 'Education level',
        name: 'educationLevel',
        dataType: 'Enum',
        defaultValue: null,
        options: [
          'PRE_PRIMARY:Pre-primary',
          'PRIMARY:Primary',
          'JUNIOR_SECONDARY:Junior Secondary',
          'SECONDARY:Secondary',
          'SENIOR_SECONDARY:Senior Secondary',
          'HIGHER:Higher',
          'UNIVERSITY:University',
          'NONE:None',
        ],
      }, {
        displayName: 'Literacy',
        name: 'literacy',
      }, {
        displayName: 'District',
        name: 'districtName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Chiefdom',
        name: 'chiefdomName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Facility',
        name: 'facilityName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Community',
        name: 'communityName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Preferred language',
        name: 'preferredLanguage',
      }, {
        displayName: 'Phone number',
        name: 'phoneNumber',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Working',
        name: 'working',
        TextCell: cell => (
          <Text style={[styles.normal, lightThemeText]}>
            {cell.value ? 'Yes' : 'No'}
          </Text>
        ),
      },
      {
        displayName: 'Actions',
        minWidth: 70,
        name: 'id',
        Cell: cell => (
          <View style={styles.buttonContainer}>
            { cell.canWrite &&
            <Button
              onPress={() => Actions.chwsEdit({ chwId: cell.value })}
              iconName="pencil-square-o"
              iconColor="#FFF"
              buttonColor="#337ab7"
            >
              Edit
            </Button>
            }
            { cell.canAssign &&
            <Button
              onPress={() => Actions.modulesToChw({ chwId: cell.value })}
              iconName="arrow-circle-o-right"
              iconColor="#FFF"
              buttonColor="#449C44"
              style={{ marginLeft: 5 }}
            >
              Assign Modules
            </Button>
            }
          </View>
        ),
        hide: !this.props.selected,
      },
    ];
  }

  fetchData() {
    this.setState({ loading: true });
    const params = !this.props.selected ? this.state.templateParams : [
      ...this.state.templateParams,
      { name: 'selected', defaultValue: this.props.selected },
    ];
    this.props.fetchChws(buildSearchParams(params))
      .then(() => {
        this.setState({ loading: false, noDataMessage: 'No data Found' });
      })
      .catch(() => {
        this.setState({ loading: false, noDataMessage: 'Error occurred' });
      });
  }

  render() {
    return (
      <View style={{ flex: 1 }}>
        <Filters
          availableFilters={this.state.templateParams}
          onFilter={filters => this.onFilter(filters)}
          onReset={() => this.onReset()}
          iconTop={3}
          iconRight={3}
        />
        <Text style={[styles.title, lightThemeText]}>
          {this.props.title}
        </Text>
        { (this.props.chwList.length > 0 && !this.state.loading) &&
        <FlatList
          data={this.props.chwList}
          renderItem={
            ({ item }) =>
              (<ListItem
                row={item}
                columns={this.getColumnDefinitions()}
                canWrite={this.state.CHW_WRITE_AUTHORITY}
                canAssign={this.state.ASSIGN_MODULES_AUTHORITY}
              />)
          }
          keyExtractor={(item, index) => index}
        />
        }
        { (this.props.chwList.length === 0 && !this.state.loading) &&
          <Text style={styles.noDataMessage}>
            {this.state.noDataMessage}
          </Text>
        }
        { this.state.loading &&
          <View style={{ paddingTop: 60 }}>
            <Spinner />
          </View>
        }
      </View>
    );
  }
}

function mapStateToProps(state) {
  return {
    chwList: state.tablesReducer.chwList,
  };
}

export default connect(mapStateToProps, { fetchChws })(HealthWorkersList);

HealthWorkersList.propTypes = {
  fetchChws: PropTypes.func.isRequired,
  chwList: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  selected: PropTypes.bool,
  title: PropTypes.string.isRequired,
};

HealthWorkersList.defaultProps = {
  selected: true,
};
