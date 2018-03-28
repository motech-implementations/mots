import React, { Component } from 'react';
import { FlatList, View } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import Button from '../components/Button';

import ListItem from '../components/ListItem';
import { fetchUsers } from '../actions/index';
import {
  MANAGE_USERS_AUTHORITY,
  MANAGE_INCHARGE_USERS_AUTHORITY,
  hasAuthority,
} from '../utils/authorization';

const COLUMNS = [
  {
    Header: 'Username',
    accessor: 'username',
  }, {
    Header: 'Email',
    accessor: 'email',
  }, {
    Header: 'Name',
    accessor: 'name',
  }, {
    Header: 'Role',
    accessor: 'roleName',
  },
  {
    Header: 'Actions',
    minWidth: 50,
    accessor: 'id',
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
    };
  }

  componentWillMount() {
    hasAuthority(MANAGE_USERS_AUTHORITY, MANAGE_INCHARGE_USERS_AUTHORITY).then((result) => {
      if (result) { this.setState({ MANAGE_USERS_AUTHORITY: true }); }
    });
    this.props.fetchUsers();
  }

  render() {
    return (
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
};
