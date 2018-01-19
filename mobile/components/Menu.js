import React from 'react';
import { View, ScrollView, Text, TouchableOpacity } from 'react-native';
import { Actions } from 'react-native-router-flux';
import Icon from 'react-native-vector-icons/FontAwesome';

import Collapsible from './Collapsible';

const styles = {
  container: {
    flex: 1,
    backgroundColor: '#FFF',
    flexDirection: 'column',
  },
  menuItem: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    borderBottomColor: '#e7e7e7',
    borderBottomWidth: 1,
    height: 50,
  },
  menuItemText: {
    fontSize: 20,
    color: '#337ab7',
    paddingLeft: 5,
  },
  iconContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: 10,
    width: 30,
  },
};

const Menu = () => (
  <ScrollView style={styles.container}>

    <TouchableOpacity onPress={Actions.home} style={styles.menuItem}>
      <View style={styles.iconContainer}>
        <Icon name="user" size={20} color="#337ab7" />
      </View>
      <Text style={styles.menuItemText}>Profile</Text>
    </TouchableOpacity>

    <TouchableOpacity onPress={Actions.home} style={styles.menuItem}>
      <View style={styles.iconContainer}>
        <Icon name="home" size={20} color="#337ab7" />
      </View>
      <Text style={styles.menuItemText}>Home</Text>
    </TouchableOpacity>

    <Collapsible title="CHW" headerIcon="users" style={styles.menuItem}>
      <View>
        <TouchableOpacity onPress={Actions.home} style={styles.menuItem}>
          <View style={[styles.iconContainer, { marginLeft: 30 }]}>
            <Icon name="plus" size={20} color="#337ab7" />
          </View>
          <Text style={styles.menuItemText}>Add CHW</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={Actions.home} style={styles.menuItem}>
          <View style={[styles.iconContainer, { marginLeft: 30 }]}>
            <Icon name="list" size={20} color="#337ab7" />
          </View>
          <Text style={styles.menuItemText}>CHW List</Text>
        </TouchableOpacity>
      </View>
    </Collapsible>

    <Collapsible title="Modules" headerIcon="graduation-cap" style={styles.menuItem}>
      <TouchableOpacity onPress={Actions.home} style={styles.menuItem}>
        <View style={[styles.iconContainer, { marginLeft: 30 }]}>
          <Icon name="check" size={20} color="#337ab7" />
        </View>
        <Text style={styles.menuItemText}>Assign</Text>
      </TouchableOpacity>
    </Collapsible>

    <Collapsible title="Incharge" headerIcon="user" style={styles.menuItem}>
      <View>
        <TouchableOpacity onPress={Actions.home} style={styles.menuItem}>
          <View style={[styles.iconContainer, { marginLeft: 30 }]}>
            <Icon name="plus" size={20} color="#337ab7" />
          </View>
          <Text style={styles.menuItemText}>Add Incharge</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={Actions.incharges} style={styles.menuItem}>
          <View style={[styles.iconContainer, { marginLeft: 30 }]}>
            <Icon name="list" size={20} color="#337ab7" />
          </View>
          <Text style={styles.menuItemText}>Incharge List</Text>
        </TouchableOpacity>
      </View>
    </Collapsible>

    <TouchableOpacity onPress={Actions.home} style={styles.menuItem}>
      <View style={styles.iconContainer}>
        <Icon name="bar-chart" size={20} color="#337ab7" />
      </View>
      <Text style={styles.menuItemText}>Reports</Text>
    </TouchableOpacity>

  </ScrollView>
);

export default Menu;
