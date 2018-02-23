import React, { Component } from 'react';
import {
  View,
  Alert,
  Text,
  ScrollView,
} from 'react-native';
import { Select, Option } from 'react-native-chooser';
import { TagSelect } from 'react-native-tag-select';
import PropTypes from 'prop-types';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import DatePicker from 'react-native-datepicker';

import apiClient from '../utils/api-client';
import formsStyles from '../styles/formsStyles';
import modulesStyles from '../styles/modulesStyles';
import Button from './Button';
import getContainerStyle from '../utils/styleUtils';

const { formHeader, buttonContainer } = formsStyles;
const {
  modulesContainer, labelText, itemSelected,
  fieldRow, selectField, datePickerStyle,
} = modulesStyles;

class AssignModulesToDistrict extends Component {
  constructor(props) {
    super(props);
    this.state = {
      availableModulesList: [],
      districts: [],
      selectedDistrict: {},
      startDate: '',
      endDate: '',
    };

    this.onSelect = this.onSelect.bind(this);
    this.sendAssignedModules = this.sendAssignedModules.bind(this);
  }

  componentWillMount() {
    this.fetchDistricts();
    this.fetchAvailableModules();
  }

  onSelect(value, label) {
    this.setState({
      selectedDistrict: {
        value, label,
      },
    });
  }

  fetchDistricts() {
    const url = '/api/districtsOnly';

    return apiClient.get(url)
      .then((response) => {
        if (response) {
          const districts = response.map(district => (
            { value: district.id, label: district.name }
          ));
          this.setState({ districts });
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
    if (this.state.selectedDistrict.value && selectedModules &&
        this.state.startDate && this.state.endDate) {
      const url = '/api/module/district/assign';

      const payload = {
        modules: selectedModules.map(module => module.id),
        districtId: this.state.selectedDistrict.value,
        startDate: this.state.startDate,
        endDate: this.state.endDate,
      };

      const callback = () => {
        if (!this.props.fetchError) {
          Alert.alert(
            'Success!',
            'Modules have been assigned!',
            [{ text: 'OK', onPress: () => Actions.chws() }],
            { cancelable: false },
          );
        }
      };

      apiClient.post(url, payload)
        .then(() => callback());
    } else {
      Alert.alert(
        '',
        'You need to select district, start date, end date ' +
        'and module to finish assignment.',
        [{ text: 'OK' }],
        { cancelable: false },
      );
    }
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <Text style={formHeader}>Assign Modules to a District</Text>
        <ScrollView style={modulesContainer}>
          <View style={fieldRow}>
            <Text style={labelText}>District:</Text>
            <Select
              onSelect={this.onSelect}
              defaultText={this.state.selectedDistrict.label || 'Click to Select'}
              style={selectField}
              textStyle={{}}
              transparent
              optionListStyle={{ backgroundColor: '#FFF' }}
            >
              {this.state.districts.map(district => (
                <Option key={district.value} value={district.value}>{district.label}</Option>
              ))}
            </Select>
          </View>
          <View style={fieldRow}>
            <Text style={labelText}>Start Date:</Text>
            <DatePicker
              style={datePickerStyle}
              format="YYYY-MM-DD"
              timeFormat={false}
              closeOnSelect
              placeholder="Select a date"
              date={this.state.startDate}
              onDateChange={(date) => { this.setState({ startDate: date }); }}
            />
          </View>
          <View style={fieldRow}>
            <Text style={labelText}>End Date:</Text>
            <DatePicker
              style={datePickerStyle}
              format="YYYY-MM-DD"
              timeFormat={false}
              closeOnSelect
              placeholder="Select a date"
              date={this.state.endDate}
              onDateChange={(date) => { this.setState({ endDate: date }); }}
            />
          </View>
          <View>
            <Text style={[labelText, { marginBottom: 15 }]}>Select modules to assign:</Text>
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
                marginLeft={10}
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

export default connect(mapStateToProps)(AssignModulesToDistrict);

AssignModulesToDistrict.propTypes = {
  fetchError: PropTypes.bool.isRequired,
};
