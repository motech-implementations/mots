import React, { Component } from 'react';
import { FlatList, View, Text } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import Button from '../components/Button';
import Spinner from '../components/Spinner';

import ListItem from '../components/ListItem';
import Filters from '../components/Filters';
import { fetchUsers } from '../actions/index';
import {
  MANAGE_USERS_AUTHORITY,
  MANAGE_INCHARGE_USERS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';
import styles from '../styles/listsStyles';
import commonStyles from '../styles/commonStyles';
import buildSearchParams from '../utils/search-params';

const { lightThemeText } = commonStyles;

const COLUMNS = [
  {
    displayName: 'Username',
    name: 'username',
    dataType: 'String',
    defaultValue: null,
  }, {
    displayName: 'Email',
    name: 'email',
    dataType: 'String',
    defaultValue: null,
  }, {
    displayName: 'Name',
    name: 'name',
    dataType: 'String',
    defaultValue: null,
  }, {
    displayName: 'Role',
    name: 'role',
    dataType: 'String',
    defaultValue: null,
  },
  {
    displayName: 'Actions',
    minWidth: 50,
    name: 'id',
    Cell: cell => (
      <View>
        { cell.canWrite &&
        <Button
          onPress={() => Actions.userEdit({ userId: cell.value })}
          iconName="pencil-square-o"
          iconColor="#FFF"
          buttonColor="#337ab7"
        >
          Edit
        </Button>
        }
      </View>
    ),
  },
];

class UserList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      MANAGE_USERS_AUTHORITY: false,
      templateParams: COLUMNS,
      loading: false,
      noDataMessage: 'No data found',
    };
  }

  componentWillMount() {
    hasAuthority(MANAGE_USERS_AUTHORITY, MANAGE_INCHARGE_USERS_AUTHORITY).then((result) => {
      if (result) { this.setState({ MANAGE_USERS_AUTHORITY: true }); }
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

  fetchData() {
    this.setState({ loading: true });
    this.props.fetchUsers(buildSearchParams(this.state.templateParams))
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
        { (this.props.users.length > 0 && !this.state.loading) &&
        <FlatList
          data={this.props.users}
          renderItem={
          ({ item }) =>
            (<ListItem
              row={item}
              columns={COLUMNS}
              canWrite={this.state.MANAGE_USERS_AUTHORITY}
            />)
        }
          keyExtractor={(item, index) => index}
        />
        }
        { (this.props.users.length === 0 && !this.state.loading) &&
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
    users: state.tablesReducer.userList,
  };
}

export default connect(mapStateToProps, { fetchUsers })(UserList);

UserList.propTypes = {
  fetchUsers: PropTypes.func.isRequired,
  users: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  title: PropTypes.string.isRequired,
};
