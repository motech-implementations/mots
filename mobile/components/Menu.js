import React, { Component } from 'react';
import { View, ScrollView, Text, TouchableOpacity } from 'react-native';
import { Actions, ActionConst } from 'react-native-router-flux';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';

import Collapsible from './Collapsible';
import { signoutUser } from '../actions';
import { CHW_READ_AUTHORITY, ASSIGN_MODULES_AUTHORITY, CHW_WRITE_AUTHORITY,
  DISPLAY_REPORTS_AUTHORITY, INCHARGE_READ_AUTHORITY, INCHARGE_WRITE_AUTHORITY,
  MANAGE_USERS_AUTHORITY, hasAuthority } from '../utils/authorization';

const HIDE_NOT_IMPLEMENTED = true;

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
    marginTop: 20,
    marginBottom: 20,
    marginLeft: 20,
  },
};

class Menu extends Component {
  static contextTypes = {
    drawer: PropTypes.object,
  };
  constructor(props) {
    super(props);
    this.state = {
      CHW_READ_AUTHORITY: false,
      CHW_WRITE_AUTHORITY: false,
      INCHARGE_READ_AUTHORITY: false,
      INCHARGE_WRITE_AUTHORITY: false,
      ASSIGN_MODULES_AUTHORITY: false,
      DISPLAY_REPORTS_AUTHORITY: false,
      MANAGE_USERS_AUTHORITY: false,
    };
  }

  componentWillMount() {
    hasAuthority(CHW_READ_AUTHORITY).then((result) => {
      if (result) { this.setState({ CHW_READ_AUTHORITY: true }); }
    });
    hasAuthority(CHW_WRITE_AUTHORITY).then((result) => {
      if (result) { this.setState({ CHW_WRITE_AUTHORITY: true }); }
    });
    hasAuthority(INCHARGE_READ_AUTHORITY).then((result) => {
      if (result) { this.setState({ INCHARGE_READ_AUTHORITY: true }); }
    });
    hasAuthority(INCHARGE_WRITE_AUTHORITY).then((result) => {
      if (result) { this.setState({ INCHARGE_WRITE_AUTHORITY: true }); }
    });
    hasAuthority(ASSIGN_MODULES_AUTHORITY).then((result) => {
      if (result) { this.setState({ ASSIGN_MODULES_AUTHORITY: true }); }
    });
    hasAuthority(DISPLAY_REPORTS_AUTHORITY).then((result) => {
      if (result) { this.setState({ DISPLAY_REPORTS_AUTHORITY: true }); }
    });
    hasAuthority(MANAGE_USERS_AUTHORITY).then((result) => {
      if (result) { this.setState({ MANAGE_USERS_AUTHORITY: true }); }
    });
  }

  openSection(sectionKey) {
    Actions[sectionKey].call();
    this.context.drawer.close();
  }

  logout() {
    this.props.signoutUser();
    Actions.auth({ type: ActionConst.RESET });
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

          { (this.state.CHW_READ_AUTHORITY || this.state.CHW_WRITE_AUTHORITY) &&
          <Collapsible title="CHW" headerIcon="users" style={styles.menuItem}>
            <View>
              { this.state.CHW_WRITE_AUTHORITY &&
              <TouchableOpacity
                onPress={() => this.openSection('chwsNew')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="plus" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>Add CHW</Text>
              </TouchableOpacity>
              }
              { this.state.CHW_READ_AUTHORITY &&
              <TouchableOpacity
                onPress={() => this.openSection('chws')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="list" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>CHW List</Text>
              </TouchableOpacity>
              }
            </View>
          </Collapsible>
          }

          { this.state.ASSIGN_MODULES_AUTHORITY &&
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
          }

          { (this.state.INCHARGE_WRITE_AUTHORITY || this.state.INCHARGE_READ_AUTHORITY) &&
          <Collapsible title="Incharge" headerIcon="user-md" style={styles.menuItem}>
            <View>
              { this.state.INCHARGE_WRITE_AUTHORITY &&
              <TouchableOpacity
                onPress={() => this.openSection('inchargesNew')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="plus" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>Add Incharge</Text>
              </TouchableOpacity>
              }

              { this.state.INCHARGE_READ_AUTHORITY &&
              <TouchableOpacity
                onPress={() => this.openSection('incharges')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="list" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>Incharge List</Text>
              </TouchableOpacity>
              }
            </View>
          </Collapsible>
          }

          { this.state.MANAGE_USERS_AUTHORITY &&
            <Collapsible title="Users" headerIcon="user" style={styles.menuItem}>
              <View>
                <TouchableOpacity
                  onPress={() => this.openSection('home')}
                  style={styles.menuItem}
                >
                  <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                    <Icon name="plus" size={20} color="#337ab7" />
                  </View>
                  <Text style={styles.menuItemText}>Add User</Text>
                </TouchableOpacity>

                <TouchableOpacity
                  onPress={() => this.openSection('users')}
                  style={styles.menuItem}
                >
                  <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                    <Icon name="list" size={20} color="#337ab7" />
                  </View>
                  <Text style={styles.menuItemText}>User List</Text>
                </TouchableOpacity>
              </View>
            </Collapsible>
            }

          { this.state.DISPLAY_REPORTS_AUTHORITY &&
          <TouchableOpacity
            onPress={() => this.openSection('home')}
            style={styles.menuItem}
          >
            <View style={styles.iconContainer}>
              <Icon name="bar-chart" size={20} color="#337ab7" />
            </View>
            <Text style={styles.menuItemText}>Reports</Text>
          </TouchableOpacity>
          }

          { !HIDE_NOT_IMPLEMENTED &&
          <TouchableOpacity
            onPress={() => this.openSection('synchronizeView')}
            style={styles.menuItem}
          >
            <View style={styles.iconContainer}>
              <Icon name="refresh" size={20} color="#337ab7" />
            </View>
            <Text style={styles.menuItemText}>Synchronize</Text>
          </TouchableOpacity>
          }

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
