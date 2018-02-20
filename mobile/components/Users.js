import React from 'react';
import { View, Text } from 'react-native';
import UserList from '../container/UserList';
import getContainerStyle from '../utils/styleUtils';
import styles from '../styles/listsStyles';

const Users = () => (
  <View style={getContainerStyle()}>
    <Text style={styles.title}>Users</Text>
    <UserList />
  </View>
);

export default Users;
