import React, { Component } from 'react';
import {
  View,
  Text,
  ScrollView,
  PixelRatio,
} from 'react-native';
import { Select, Option } from 'react-native-chooser';
import { TagSelect } from 'react-native-tag-select';
import PropTypes from 'prop-types';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';

import apiClient from '../utils/api-client';
import formsStyles from '../styles/formsStyles';
import modulesStyles from '../styles/modulesStyles';
import inputsStyles from '../styles/inputsStyles';
import Button from './Button';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';

const { formHeader, buttonContainer } = formsStyles;
const { labelStyle, labelStyleSmall } = inputsStyles;
const {
  modulesContainer, itemSelected,
  fieldRow, selectField,
} = modulesStyles;
const { lightThemeText } = commonStyles;

class AssignModulesToGroup extends Component {
  constructor(props) {
    super(props);
    this.state = {
      availableModulesList: [],
      groups: [],
      selectedGroup: {},
    };

    this.onSelect = this.onSelect.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
  }

  componentWillMount() {
    this.fetchGroups();
    this.fetchAvailableModules();
  }

  onSelect(value, label) {
    this.setState({
      selectedGroup: {
        value, label,
      },
    });
  }

  fetchGroups() {
    apiClient.get('/api/group')
      .then((response) => {
        if (response) {
          const groups = response.map(group => ({ value: group.id, label: group.name }));
          this.setState({ groups });
        }
      });
  }

  fetchAvailableModules() {
    const url = '/api/modules/simple';

    apiClient.get(url)
      .then((response) => {
        const availableModulesList = response.map(module => (
          { id: module.id, label: module.name }
        ));
        if (response) {
          this.setState({ availableModulesList });
        }
      });
  }

  sendAssignedModules(selectedModules) {
    if (this.state.selectedGroup.value && selectedModules) {
      const url = '/api/module/group/assign';

      const payload = {
        modules: selectedModules.map(module => module.id),
        groupId: this.state.selectedGroup.value,
      };

      const callback = (assigned) => {
        if (!this.props.fetchError) {
          if (assigned) {
            Actions.modalSuccess({
              message: 'Modules have been assigned!',
              onClose: () => { Actions.chws(); },
            });
          } else {
            Actions.modalSuccess({
              message: 'Module was already assigned!',
            });
          }
        }
      };

      apiClient.post(url, payload)
        .then(response => callback(response));
    } else {
      Actions.modalInfo({
        message: 'You need to select group and module to finish assignment.',
      });
    }
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <Text style={[formHeader, lightThemeText]}>Assign Modules to a Group</Text>
        <ScrollView style={modulesContainer} alwaysBounceVertical={false}>
          <View style={fieldRow}>
            <Text style={[labelStyle, lightThemeText, PixelRatio.get() < 2 && labelStyleSmall]}>
              Group:
            </Text>
            <View style={selectField}>
              <Select
                onSelect={this.onSelect}
                defaultText={this.state.selectedGroup.label || 'Click to Select'}
                textStyle={lightThemeText}
                style={{ borderWidth: 0 }}
                transparent
                optionListStyle={{ backgroundColor: '#FFF' }}
              >
                {this.state.groups.map(group => (
                  <Option key={group.value} value={group.value} styleText={lightThemeText}>
                    {group.label}
                  </Option>
                ))}
              </Select>
            </View>
          </View>
          <View>
            <Text style={[
              labelStyle,
              lightThemeText,
              PixelRatio.get() < 2 && labelStyleSmall,
              { marginBottom: 15 },
            ]}
            >
              Select modules to assign:
            </Text>
            <TagSelect
              data={this.state.availableModulesList}
              ref={(module) => { this.module = module; }}
              itemStyleSelected={itemSelected}
            />
            <View style={buttonContainer}>
              <Button
                iconName="check"
                iconColor="#FFF"
                buttonColor="#337ab7"
                onPress={() => {
                  this.sendAssignedModules(this.module.itemsSelected);
                }}
              >
                Assign
              </Button>
              <Button
                iconName="ban"
                iconColor="#FFF"
                buttonColor="grey"
                style={{ marginLeft: 10 }}
                onPress={() => {
                  Actions.chws();
                }}
              >
                Cancel
              </Button>
            </View>
          </View>
        </ScrollView>
      </View>

    );
  }
}

function mapStateToProps(state) {
  return {
    fetchError: state.tablesReducer.fetchError,
  };
}

export default connect(mapStateToProps)(AssignModulesToGroup);

AssignModulesToGroup.propTypes = {
  fetchError: PropTypes.bool.isRequired,
};
