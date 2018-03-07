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
import inputsStyles from '../styles/inputsStyles';
import modulesStyles from '../styles/modulesStyles';
import Button from './Button';
import getContainerStyle from '../utils/styleUtils';

const { formHeader, buttonContainer } = formsStyles;
const { labelStyle, labelStyleSmall } = inputsStyles;
const {
  modulesContainer, itemSelected,
  fieldRow, selectField,
} = modulesStyles;

class AssignModulesToChw extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedModules: [],
      availableModulesList: [],
      showModuleButtons: false,
      chwList: [],
      selectedChw: {},
    };

    this.onSelect = this.onSelect.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
  }

  componentWillMount() {
    this.fetchChwInfo();
    this.fetchAvailableModules();
  }

  onSelect(value, label) {
    this.setState({
      selectedChw: {
        value, label,
      },
    }, () => this.fetchSelectedModules());
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

        if (this.props.chwId || this.state.selectedChw.value) {
          this.setState({ showModuleButtons: false });
          this.fetchSelectedModules();
        } else {
          this.setState({ showModuleButtons: true });
        }
      });
  }

  fetchSelectedModules() {
    if (this.state.selectedChw.value || this.props.chwId) {
      this.setState({ showModuleButtons: false });
      const url = `/api/assignedModules/?chwId=${this.state.selectedChw.value || this.props.chwId}`;
      apiClient.get(url)
        .then((response) => {
          const selectedModules = response.modules.map(module => module.id);
          this.setState({
            selectedModules: this.state.availableModulesList.filter(module =>
              selectedModules.find(selectedModule => module.id === selectedModule)),
            showModuleButtons: true,
          });
        });
    }
  }

  fetchChwInfo() {
    const url = '/api/chwInfo';

    apiClient.get(url)
      .then((response) => {
        if (response) {
          const chwList = response.map(chw => (
            {
              value: chw.id,
              label: `${chw.chwId}: ${chw.firstName || ''} ${chw.otherName || ''} ${chw.secondName || ''}`,
            }));
          this.setState({ chwList });
          if (this.props.chwId) {
            this.setState({ selectedChw: chwList.find(chw => chw.value === this.props.chwId) });
          }
        }
      });
  }

  sendAssignedModules(selectedModules) {
    if (this.state.selectedChw.value && selectedModules) {
      const url = '/api/module/assign';
      const modules = selectedModules.map(module => module.id);

      const payload = {
        modules,
        chwId: this.state.selectedChw.value,
      };

      const callback = () => {
        if (!this.props.fetchError) {
          Actions.modalSuccess({
            message: 'Modules have been assigned!',
            onClose: () => { Actions.chws(); },
          });
        }
      };

      apiClient.post(url, payload)
        .then(() => callback());
    }
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <Text style={formHeader}>Assign Modules to a CHW</Text>
        <ScrollView style={modulesContainer}>
          <View style={fieldRow}>
            <Text style={[labelStyle, PixelRatio.get() < 2 && labelStyleSmall]}>
              Health Worker:
            </Text>
            <Select
              onSelect={this.onSelect}
              defaultText={this.state.selectedChw.label || 'Click to Select'}
              style={selectField}
              textStyle={{}}
              transparent
              optionListStyle={{ backgroundColor: '#FFF' }}
            >
              {this.state.chwList.map(chw => (
                <Option key={chw.value} value={chw.value}>{chw.label}</Option>
              ))}
            </Select>
          </View>
          {this.state.showModuleButtons &&
          <View>
            <Text style={[
              labelStyle,
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
              value={this.state.selectedModules}
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
                marginLeft={10}
                onPress={() => {
                  Actions.chws();
                }}
              >
                Cancel
              </Button>
            </View>
          </View>
          }
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

export default connect(mapStateToProps)(AssignModulesToChw);

AssignModulesToChw.propTypes = {
  chwId: PropTypes.string,
  fetchError: PropTypes.bool.isRequired,
};

AssignModulesToChw.defaultProps = {
  chwId: null,
};
