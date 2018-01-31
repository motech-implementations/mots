import React, { Component } from 'react';
import { View, ScrollView, Text, TouchableOpacity } from 'react-native';
import { Actions } from 'react-native-router-flux';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Collapsible from './Collapsible';
import { signoutUser } from '../actions';

const styles = {
  container: {
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
  title: {
    color: '#337ab7',
    fontSize: 36,
    marginTop: 40,
    marginBottom: 20,
    marginLeft: 20,
  },
};

class Menu extends Component {
  static contextTypes = {
    drawer: PropTypes.object,
  };

  openSection(sectionKey) {
    Actions[sectionKey].call();
    this.context.drawer.close();
  }

  logout() {
    this.props.signoutUser();
    Actions.auth({ type: 'reset' });
  }

  render() {
    return (
      <View style={{ flex: 1 }}>
        <Text style={styles.title}>Menu</Text>
        <ScrollView style={styles.container}>
          <TouchableOpacity
            onPress={() => this.openSection('home')}
            style={styles.menuItem}
          >
            <View style={styles.iconContainer}>
              <Icon name="user" size={20} color="#337ab7" />
            </View>
            <Text style={styles.menuItemText}>Profile</Text>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={() => this.openSection('home')}
            style={styles.menuItem}
          >
            <View style={styles.iconContainer}>
              <Icon name="home" size={20} color="#337ab7" />
            </View>
            <Text style={styles.menuItemText}>Home</Text>
          </TouchableOpacity>

          <Collapsible title="CHW" headerIcon="users" style={styles.menuItem}>
            <View>
              <TouchableOpacity
                onPress={() => this.openSection('home')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="plus" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>Add CHW</Text>
              </TouchableOpacity>
              <TouchableOpacity
                onPress={() => this.openSection('chws')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="list" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>CHW List</Text>
              </TouchableOpacity>
            </View>
          </Collapsible>

          <Collapsible title="Modules" headerIcon="graduation-cap" style={styles.menuItem}>
            <TouchableOpacity
              onPress={() => this.openSection('home')}
              style={styles.menuItem}
            >
              <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                <Icon name="check" size={20} color="#337ab7" />
              </View>
              <Text style={styles.menuItemText}>Assign</Text>
            </TouchableOpacity>
          </Collapsible>

          <Collapsible title="Incharge" headerIcon="user" style={styles.menuItem}>
            <View>
              <TouchableOpacity
                onPress={() => this.openSection('home')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="plus" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>Add Incharge</Text>
              </TouchableOpacity>
              <TouchableOpacity
                onPress={() => this.openSection('incharges')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="list" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>Incharge List</Text>
              </TouchableOpacity>
            </View>
          </Collapsible>

          <TouchableOpacity
            onPress={() => this.openSection('home')}
            style={styles.menuItem}
          >
            <View style={styles.iconContainer}>
              <Icon name="bar-chart" size={20} color="#337ab7" />
            </View>
            <Text style={styles.menuItemText}>Reports</Text>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={() => this.logout()}
            style={styles.menuItem}
          >
            <View style={styles.iconContainer}>
              <Icon name="sign-out" size={20} color="#337ab7" />
            </View>
            <Text style={styles.menuItemText}>Logout</Text>
          </TouchableOpacity>

        </ScrollView>
      </View>
    );
  }
}

export default connect(null, { signoutUser })(Menu);

Menu.propTypes = {
  signoutUser: PropTypes.func.isRequired,
};
