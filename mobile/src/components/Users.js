import React from 'react';
import { View } from 'react-native';
import UserList from '../container/UserList';
import getContainerStyle from '../utils/styleUtils';

const Users = () => (
  <View style={getContainerStyle()}>
    <UserList title="Users" />
  </View>
);

export default Users;
