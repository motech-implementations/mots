import React, { Component } from 'react';
import { View, ScrollView, Text, TouchableOpacity, NetInfo } from 'react-native';
import { Actions, ActionConst } from 'react-native-router-flux';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { getStatusBarHeight } from 'react-native-status-bar-height';

import { dispatch } from '../App';

import Collapsible from './Collapsible';
import { fetchReportTemplates, signoutUser, setLastOnlineTime } from '../actions';
import {
  CHW_READ_AUTHORITY, ASSIGN_MODULES_AUTHORITY, CHW_WRITE_AUTHORITY,
  DISPLAY_REPORTS_AUTHORITY, MANAGE_USERS_AUTHORITY, hasAuthority,
} from '../utils/authorization';

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
    fontSize: 19,
    color: '#337ab7',
    paddingLeft: 5,
  },
  lastOnlineText: {
    color: '#B00020',
    textAlign: 'center',
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
    hasAuthority(ASSIGN_MODULES_AUTHORITY).then((result) => {
      if (result) { this.setState({ ASSIGN_MODULES_AUTHORITY: true }); }
    });
    hasAuthority(DISPLAY_REPORTS_AUTHORITY).then((result) => {
      if (result) {
        this.setState({ DISPLAY_REPORTS_AUTHORITY: true });
        if (this.props.isConnected && this.props.expirationTime === null) {
          this.props.fetchReportTemplates();
        }
      }
    });
    hasAuthority(MANAGE_USERS_AUTHORITY).then((result) => {
      if (result) { this.setState({ MANAGE_USERS_AUTHORITY: true }); }
    });
  }

  componentDidMount() {
    NetInfo.isConnected.fetch().then((isConnected) => {
      dispatch(setLastOnlineTime(isConnected));
    });

    NetInfo.isConnected.addEventListener(
      'connectionChange',
      isConnected => dispatch(setLastOnlineTime(isConnected)),
    );
  }

  componentWillUnmount() {
    NetInfo.isConnected.removeEventListener(
      'connectionChange',
      isConnected => dispatch(setLastOnlineTime(isConnected)),
    );
  }

  openSection(sectionKey) {
    Actions[sectionKey].call();
    this.context.drawer.close();
  }

  openReportSection(props) {
    if (this.props.currentScene.indexOf('report') !== -1) {
      Actions.refresh(props);
    } else {
      Actions.report(props);
    }
    this.context.drawer.close();
  }

  logout() {
    this.props.signoutUser();
    Actions.auth({ type: ActionConst.RESET });
  }

  renderReports() {
    if (this.props.reportTemplates) {
      return (
        <View>
          {
            this.props.reportTemplates.map(report => (
              <TouchableOpacity
                onPress={() => this.openReportSection({
                  reportName: report.name,
                  reportId: report.id,
                  templateParameters: report.templateParameters,
                })}
                style={styles.menuItem}
                key={report.id}
              >
                <View style={[styles.iconContainer, { marginLeft: 15 }]}>
                  <Icon name="file-text-o" size={14} color="#337ab7" />
                </View>
                <Text style={[styles.menuItemText, { fontSize: 14 }]}>{report.name.replace(' Report', '')}
                </Text>
              </TouchableOpacity>
            ))
          }
        </View>
      );
    }
    return (
      <View />
    );
  }

  render() {
    return (
      <View style={{ flex: 1, paddingTop: getStatusBarHeight(true) }}>
        <Text style={styles.title}>Menu</Text>
        {!this.props.isConnected && this.props.lastOnlineTime !== null &&
          <Text style={[styles.lastOnlineText]}>
            <Icon name="info-circle" />
            {` Offline since ${new Date(this.props.lastOnlineTime).toLocaleTimeString()} ${new Date(this.props.lastOnlineTime).toLocaleDateString()}`}
          </Text>
        }

        <ScrollView style={styles.container} alwaysBounceVertical={false}>
          {this.props.isConnected &&
          <TouchableOpacity
            onPress={() => this.openSection('profile')}
            style={styles.menuItem}
          >
            <View style={styles.iconContainer}>
              <Icon name="user" size={20} color="#337ab7" />
            </View>
            <Text style={styles.menuItemText}>Profile</Text>
          </TouchableOpacity>
          }

          <TouchableOpacity
            onPress={() => this.openSection('home')}
            style={styles.menuItem}
          >
            <View style={styles.iconContainer}>
              <Icon name="home" size={20} color="#337ab7" />
            </View>
            <Text style={styles.menuItemText}>Home</Text>
          </TouchableOpacity>

          { this.props.isConnected
          && (this.state.CHW_READ_AUTHORITY || this.state.CHW_WRITE_AUTHORITY) &&
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
              <View>
                <TouchableOpacity
                  onPress={() => this.openSection('allChws')}
                  style={styles.menuItem}
                >
                  <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                    <Icon name="list" size={20} color="#337ab7" />
                  </View>
                  <Text style={styles.menuItemText}>CHW List</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => this.openSection('chws')}
                  style={styles.menuItem}
                >
                  <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                    <Icon name="list" size={20} color="#337ab7" />
                  </View>
                  <Text style={styles.menuItemText}>Selected CHW List</Text>
                </TouchableOpacity>
              </View>
              }
            </View>
          </Collapsible>
          }

          { this.props.isConnected && this.state.ASSIGN_MODULES_AUTHORITY &&
          <Collapsible title="Modules" headerIcon="graduation-cap" style={styles.menuItem}>
            <View>
              <TouchableOpacity
                onPress={() => this.openSection('modulesToLocation')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="check" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>Location assignment</Text>
              </TouchableOpacity>
              <TouchableOpacity
                onPress={() => this.openSection('modulesToGroup')}
                style={styles.menuItem}
              >
                <View style={[styles.iconContainer, { marginLeft: 30 }]}>
                  <Icon name="check" size={20} color="#337ab7" />
                </View>
                <Text style={styles.menuItemText}>Group assignment</Text>
              </TouchableOpacity>
            </View>
          </Collapsible>
          }

          { this.state.DISPLAY_REPORTS_AUTHORITY &&
          <Collapsible title="Reports" headerIcon="bar-chart" style={styles.menuItem}>
            {this.renderReports()}
          </Collapsible>
          }

          { this.props.isConnected && this.state.MANAGE_USERS_AUTHORITY &&
            <Collapsible title="Users" headerIcon="user" style={styles.menuItem}>
              <View>
                <TouchableOpacity
                  onPress={() => this.openSection('userNew')}
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

function mapStateToProps(state) {
  return {
    reportTemplates: state.reportReducer.templates,
    isConnected: state.connectionReducer.isConnected,
    currentScene: state.sceneReducer.currentScene,
    expirationTime: state.auth.expirationTime,
    lastOnlineTime: state.lastOnlineReducer.lastOnlineTime,
  };
}

export default connect(mapStateToProps, { signoutUser, fetchReportTemplates })(Menu);

Menu.propTypes = {
  reportTemplates: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  isConnected: PropTypes.bool.isRequired,
  lastOnlineTime: PropTypes.number,
  signoutUser: PropTypes.func.isRequired,
  fetchReportTemplates: PropTypes.func.isRequired,
  currentScene: PropTypes.string,
  expirationTime: PropTypes.number,
};

Menu.defaultProps = {
  currentScene: '',
  expirationTime: null,
  lastOnlineTime: null,
};
