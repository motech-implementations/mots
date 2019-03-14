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
import DatePicker from 'react-native-datepicker';

import apiClient from '../utils/api-client';
import formsStyles from '../styles/formsStyles';
import modulesStyles from '../styles/modulesStyles';
import inputsStyles from '../styles/inputsStyles';
import Button from './Button';
import getContainerStyle from '../utils/styleUtils';
import commonStyles from '../styles/commonStyles';
import { fetchLocations } from '../actions';
import { getSelectableLocations } from '../utils/form-utils';

const { formHeader, buttonContainer } = formsStyles;
const { labelStyle, labelStyleSmall } = inputsStyles;
const {
  modulesContainer, itemSelected,
  fieldRow, selectField, datePickerStyle, dateInput,
} = modulesStyles;
const { lightThemeText } = commonStyles;

class AssignModulesToLocation extends Component {
  constructor(props) {
    super(props);
    this.state = {
      availableModulesList: [],
      chiefdomOptions: [],
      facilityOptions: [],
      selectedDistrict: {},
      selectedChiefdom: {},
      selectedFacility: {},
      startDate: '',
      endDate: '',
    };

    this.sendAssignedModules = this.sendAssignedModules.bind(this);
    this.handleDistrictChange = this.handleDistrictChange.bind(this);
    this.handleChiefdomChange = this.handleChiefdomChange.bind(this);
    this.handleFacilityChange = this.handleFacilityChange.bind(this);
  }

  static getOptions(locations) {
    return locations.map(district => (
      <Option key={district.value} value={district.value} styleText={lightThemeText}>
        {district.label}
      </Option>
    ));
  }

  componentWillMount() {
    this.props.fetchLocations();
    this.fetchAvailableModules();
  }


  getChiefdomOptions(selectedDistrictId) {
    if (selectedDistrictId != null) {
      const chiefdoms = getSelectableLocations(
        'chiefdoms',
        this.props.availableLocations,
        selectedDistrictId,
      );
      return chiefdoms.map(chiefdom => ({ value: chiefdom.id, label: chiefdom.name }));
    }
    return [];
  }

  getFacilityOptions(selectedChiefdomId) {
    if (selectedChiefdomId != null) {
      const facilities = getSelectableLocations(
        'facilities',
        this.props.availableLocations,
        this.state.selectedDistrict.value,
        selectedChiefdomId,
      );
      return facilities.map(facility => ({ value: facility.id, label: facility.name }));
    }
    return [];
  }

  handleDistrictChange = (value, label) => {
    this.setState({
      selectedDistrict: {
        value, label,
      },
      selectedChiefdom: {},
      selectedFacility: {},
      chiefdomOptions: this.getChiefdomOptions(value),
    });
  };

  handleChiefdomChange = (value, label) => {
    this.setState({
      selectedChiefdom: {
        value, label,
      },
      selectedFacility: {},
      facilityOptions: this.getFacilityOptions(value),
    });
  };

  handleFacilityChange = (value, label) => {
    this.setState({
      selectedFacility: {
        value, label,
      },
    });
  };

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
      if (this.state.selectedChiefdom.value) {
        payload.chiefdomId = this.state.selectedChiefdom.value;
      }
      if (this.state.selectedFacility.value) {
        payload.facilityId = this.state.selectedFacility.value;
      }

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
    } else {
      Actions.modalInfo({
        message: 'You need to select district, start date, end date ' +
        'and module to finish assignment.',
      });
    }
  }

  render() {
    return (
      <View style={getContainerStyle()}>
        <Text style={[formHeader, lightThemeText]}>Assign Modules to a location</Text>
        <ScrollView style={modulesContainer} alwaysBounceVertical={false}>
          <View style={fieldRow}>
            <Text style={[labelStyle, lightThemeText, PixelRatio.get() < 2 && labelStyleSmall]}>
              District:
            </Text>
            <View style={selectField}>
              <Select
                onSelect={this.handleDistrictChange}
                defaultText={this.state.selectedDistrict.label || 'Click to Select'}
                textStyle={lightThemeText}
                style={{ borderWidth: 0 }}
                transparent
                optionListStyle={{ backgroundColor: '#FFF' }}
              >
                {AssignModulesToLocation.getOptions(this.props.districtOptions)}
              </Select>
            </View>
          </View>
          {this.state.selectedDistrict.value &&
          <View style={fieldRow}>
            <Text style={[labelStyle, lightThemeText,
              PixelRatio.get() < 2 && labelStyleSmall]}>
              Chiefdom:
            </Text>
            <View style={selectField}>
              <Select
                onSelect={this.handleChiefdomChange}
                defaultText={this.state.selectedChiefdom.label
                || 'Click to Select (optional)'}
                textStyle={lightThemeText}
                style={{ borderWidth: 0 }}
                transparent
                optionListStyle={{ backgroundColor: '#FFF' }}
              >
                {AssignModulesToLocation.getOptions(this.state.chiefdomOptions)}
              </Select>
            </View>
          </View>
          }
          {this.state.selectedChiefdom.value &&
          <View style={fieldRow}>
            <Text style={[labelStyle, lightThemeText,
              PixelRatio.get() < 2 && labelStyleSmall]}>
              Facility:
            </Text>
            <View style={selectField}>
              <Select
                onSelect={this.handleFacilityChange}
                defaultText={this.state.selectedFacility.label
                || 'Click to Select (optional)'}
                textStyle={lightThemeText}
                style={{ borderWidth: 0 }}
                transparent
                optionListStyle={{ backgroundColor: '#FFF' }}
              >
                {AssignModulesToLocation.getOptions(this.state.facilityOptions)}
              </Select>
            </View>
          </View>
          }
          <View style={fieldRow}>
            <Text style={[
              labelStyle,
              lightThemeText,
              PixelRatio.get() < 2 && labelStyleSmall]}
            >
              Start Date:
            </Text>
            <DatePicker
              style={datePickerStyle}
              format="YYYY-MM-DD"
              timeFormat={false}
              closeOnSelect
              placeholder="Select a date"
              confirmBtnText="Confirm"
              cancelBtnText="Cancel"
              customStyles={{
                placeholderText: lightThemeText,
                dateInput,
              }}
              date={this.state.startDate}
              onDateChange={(date) => { this.setState({ startDate: date }); }}
            />
          </View>
          <View style={fieldRow}>
            <Text style={[
              labelStyle,
              lightThemeText,
              PixelRatio.get() < 2 && labelStyleSmall]}
            >
              End Date:
            </Text>
            <DatePicker
              style={datePickerStyle}
              format="YYYY-MM-DD"
              timeFormat={false}
              closeOnSelect
              placeholder="Select a date"
              confirmBtnText="Confirm"
              cancelBtnText="Cancel"
              customStyles={{
                placeholderText: lightThemeText,
                dateInput,
              }}
              date={this.state.endDate}
              onDateChange={(date) => { this.setState({ endDate: date }); }}
            />
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
  console.log(state.availableLocations);
  const districtOptions = (state.availableLocations) ? state.availableLocations
    .map(district => ({ value: district.id, label: district.name })) : [];
  return {
    districtOptions,
    availableLocations: state.availableLocations,
    fetchError: state.tablesReducer.fetchError,
  };
}

export default connect(mapStateToProps, { fetchLocations })(AssignModulesToLocation);

AssignModulesToLocation.propTypes = {
  fetchError: PropTypes.bool.isRequired,
  fetchLocations: PropTypes.func.isRequired,
  availableLocations: PropTypes.arrayOf(PropTypes.shape({})),
  districtOptions: PropTypes.arrayOf(PropTypes.shape({})),
};

AssignModulesToLocation.defaultProps = {
  availableLocations: [],
  districtOptions: [],
};
