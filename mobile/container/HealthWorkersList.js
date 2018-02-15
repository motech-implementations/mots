import React, { Component } from 'react';
import { ScrollView, View } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import ListItems from '../components/ListItems';
import { fetchChws } from '../actions/index';
import Button from '../components/Button';
import {
  CHW_WRITE_AUTHORITY, ASSIGN_MODULES_AUTHORITY,
  hasAuthority } from '../utils/authorization';

import styles from '../styles/listsStyles';

const COLUMNS = [
  {
    Header: 'ID',
    accessor: 'chwId',
  }, {
    Header: 'First name',
    accessor: 'firstName',
  }, {
    Header: 'Surname',
    accessor: 'secondName',
  }, {
    Header: 'Other name',
    accessor: 'otherName',
  }, {
    Header: 'DOB',
    accessor: 'dateOfBirth',
  }, {
    Header: 'Gender',
    accessor: 'gender',
  }, {
    Header: 'Education level',
    accessor: 'educationLevel',
  }, {
    Header: 'Literacy',
    accessor: 'literacy',
  }, {
    Header: 'Community',
    accessor: 'communityName',
  }, {
    Header: 'Preferred language',
    accessor: 'preferredLanguage',
  }, {
    Header: 'Phone number',
    accessor: 'phoneNumber',
  },
  {
    Header: 'Actions',
    minWidth: 70,
    accessor: 'id',
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
          onPress={Actions.home}
          iconName="arrow-circle-o-right"
          iconColor="#FFF"
          buttonColor="#449C44"
          marginLeft={5}
        >
          Assign Modules
        </Button>
        }
      </View>
    ),
  },
];


class HealthWorkersList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      CHW_WRITE_AUTHORITY: false,
      ASSIGN_MODULES_AUTHORITY: false,
    };
  }

  componentWillMount() {
    hasAuthority(CHW_WRITE_AUTHORITY).then((result) => {
      if (result) { this.setState({ CHW_WRITE_AUTHORITY: true }); }
    });
    hasAuthority(ASSIGN_MODULES_AUTHORITY).then((result) => {
      if (result) { this.setState({ ASSIGN_MODULES_AUTHORITY: true }); }
    });
    this.props.fetchChws();
  }

  render() {
    return (
      <ScrollView>
        <ListItems
          data={this.props.chwList}
          columns={COLUMNS}
          canWrite={this.state.CHW_WRITE_AUTHORITY}
          canAssign={this.state.ASSIGN_MODULES_AUTHORITY}
        />
      </ScrollView>
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
};
