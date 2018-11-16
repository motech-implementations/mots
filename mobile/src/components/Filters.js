import PropTypes from 'prop-types';
import React, { Component } from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  Modal,
} from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import DatePicker from 'react-native-datepicker';
import { Select, Option } from 'react-native-chooser';

import Button from './Button';
import InputWithLabel from './InputWithLabel';
import formsStyles from '../styles/formsStyles';
import inputsStyles from '../styles/inputsStyles';
import modulesStyles from '../styles/modulesStyles';
import commonStyles from '../styles/commonStyles';
import filtersStyles from '../styles/filtersStyles';

const { datePickerStyle, dateInput } = modulesStyles;
const {
  containerStyle, labelStyle, labelSelectFieldStyle, optionListStyle, labelFieldStyle,
} = inputsStyles;
const { lightThemeText } = commonStyles;
export const STATIC_FILTERS = ['pageSize', 'offset', 'orderBy'];

export default class Filters extends Component {
  constructor(props) {
    super(props);
    this.state = {
      modalVisible: false,
      availableFilters: [],
      staticFilters: STATIC_FILTERS,
    };

    this.onFilter = this.onFilter.bind(this);
    this.onClose = this.onClose.bind(this);
    this.onPress = this.onPress.bind(this);
    this.onModalPress = this.onModalPress.bind(this);
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      availableFilters: nextProps.availableFilters,
    });
  }

  onFilter(filters) {
    this.props.onFilter(filters);
    this.setState({
      modalVisible: false,
    });
  }

  onReset() {
    this.props.onReset();
    this.setState({
      modalVisible: false,
    });
  }

  onClose() {
    this.setState({
      modalVisible: false,
    });
  }

  onPress() {
    this.setState({
      modalVisible: !this.state.modalVisible,
    });
  }

  onModalPress() {
    this.setState({
      modalVisible: false,
    });
  }

  setFilterValue(value, field) {
    const filters = this.state.availableFilters.map((filter) => {
      if (filter.name === field.name) {
        return {
          ...filter,
          defaultValue: value,
        };
      }
      return filter;
    });
    this.setState({ availableFilters: filters });
  }

  renderFiltersForm() {
    const fields = this.state.availableFilters.map((field) => {
      if (field.dataType === 'String' && !this.state.staticFilters.includes(field.name)) {
        return (
          <InputWithLabel
            key={field.name}
            label={field.displayName}
            onChangeText={(value) => {
              this.setFilterValue(value, field);
            }}
            value={field.defaultValue}
          />
        );
      } else if (field.dataType === 'Date') {
        return (
          <View key={field.name} style={containerStyle}>
            <Text style={[labelStyle, lightThemeText]}>{field.displayName}</Text>
            <DatePicker
              style={datePickerStyle}
              format="YYYY-MM-DD"
              timeFormat={false}
              closeOnSelect
              placeholder="Select a date"
              customStyles={{
                placeholderText: lightThemeText,
                dateInput,
              }}
              date={field.defaultValue}
              onDateChange={(value) => {
                this.setFilterValue(value, field);
              }}
            />
          </View>
        );
      } else if (field.dataType === 'Enum') {
        const emptyValue = '';
        const availableOptions = field.options.map((option) => {
          const split = option.split(':');
          return { value: split[0], name: split[1] };
        });
        const selected = availableOptions.find(option => option.value === field.defaultValue);
        return (
          <View key={field.name} style={[containerStyle, { marginBottom: 1 }]}>
            <Text style={[labelStyle, lightThemeText]}>{field.displayName}</Text>
            <View style={[labelFieldStyle, filtersStyles.selectContainer]}>
              <Select
                transparent
                defaultText={field.defaultValue && selected ? selected.name : 'Click to Select'}
                onSelect={(value) => {
                  this.setFilterValue(value, field);
                }}
                optionListStyle={optionListStyle}
                style={labelSelectFieldStyle}
                textStyle={lightThemeText}
              >
                { availableOptions && availableOptions.map(option =>
                  (
                    <Option key={option.value} value={option.value} styleText={lightThemeText}>
                      {option.name}
                    </Option>
                  ))
                }
                <Option key="undefined" value={null} styleText={lightThemeText}>
                  {emptyValue}
                </Option>
              </Select>
            </View>
          </View>
        );
      }
      return null;
    });
    return (
      <View style={{ flex: 1 }}>
        <ScrollView style={{ paddingRight: 10 }}>
          {fields}
        </ScrollView>
      </View>
    );
  }

  render() {
    const {
      transparent,
      animationType,
      iconTop,
      iconRight,
      iconBottom,
      iconLeft,
    } = this.props;

    const iconPosition = {
      top: iconTop,
      right: iconRight,
      bottom: iconBottom,
      left: iconLeft,
    };

    return (
      <View>
        <TouchableOpacity
          style={[filtersStyles.iconContainer, iconPosition]}
          onPress={this.onPress}
        >
          <View style={filtersStyles.icon}>
            <Icon name="filter" size={16} color="#000" />
          </View>
        </TouchableOpacity>

        <Modal
          transparent={transparent}
          animationType={animationType}
          visible={this.state.modalVisible}
          onRequestClose={this.onClose}
        >
          <View style={{ flex: 1 }}>
            <View style={filtersStyles.headerContainer}>
              <Text style={filtersStyles.header}>
                FILTERS
              </Text>
            </View>

            {this.renderFiltersForm()}

            <View style={[formsStyles.buttonContainer, filtersStyles.buttonContainer]}>
              <Button
                onPress={() => this.onReset()}
                iconName="ban"
                iconColor="#FFF"
                buttonColor="grey"
              >
                Reset
              </Button>
              <Button
                onPress={() => this.onFilter(this.state.availableFilters)}
                iconName="check"
                iconColor="#FFF"
                buttonColor="#337ab7"
                style={{ marginLeft: 10 }}
              >
                Filter
              </Button>
            </View>
          </View>
        </Modal>
      </View>
    );
  }
}

Filters.propTypes = {
  onFilter: PropTypes.func,
  onReset: PropTypes.func,
  transparent: PropTypes.bool,
  animationType: PropTypes.string,
  availableFilters: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  iconTop: PropTypes.number,
  iconRight: PropTypes.number,
  iconBottom: PropTypes.number,
  iconLeft: PropTypes.number,
};

Filters.defaultProps = {
  onFilter: () => {},
  onReset: () => {},
  transparent: false,
  animationType: 'slide',
  iconTop: null,
  iconRight: null,
  iconBottom: null,
  iconLeft: null,
};
