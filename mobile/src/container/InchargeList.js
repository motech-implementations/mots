import React, { Component } from 'react';
import { FlatList, View, Text } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import Button from '../components/Button';
import Spinner from '../components/Spinner';

import ListItem from '../components/ListItem';
import Filters from '../components/Filters';
import { fetchIncharges } from '../actions/index';
import { INCHARGE_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';
import styles from '../styles/listsStyles';
import commonStyles from '../styles/commonStyles';
import buildSearchParams from '../utils/search-params';

const { lightThemeText } = commonStyles;

class InchargeList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      INCHARGE_WRITE_AUTHORITY: false,
      templateParams: this.getColumnDefinitions(),
      loading: false,
      noDataMessage: 'No data found',
    };
  }

  componentWillMount() {
    hasAuthority(INCHARGE_WRITE_AUTHORITY).then((result) => {
      if (result) { this.setState({ INCHARGE_WRITE_AUTHORITY: true }); }
    });
    this.fetchData();
  }

  onFilter(filters) {
    this.setState({
      templateParams: filters,
    }, () => this.fetchData());
  }

  onReset() {
    this.setState({
      templateParams: this.state.templateParams.map(param => ({
        ...param,
        defaultValue: null,
      })),
    }, () => this.fetchData());
  }

  getColumnDefinitions() {
    return [
      {
        displayName: 'First name',
        name: 'firstName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Surname',
        name: 'secondName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Other name',
        name: 'otherName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Phone number',
        name: 'phoneNumber',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Email',
        name: 'email',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Facility',
        name: 'facilityName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Facility ID',
        name: 'facilityIdentifier',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'Chiefdom',
        name: 'chiefdomName',
        dataType: 'String',
        defaultValue: null,
      }, {
        displayName: 'District',
        name: 'districtName',
        dataType: 'String',
        defaultValue: null,
      },
      {
        displayName: 'Actions',
        minWidth: 50,
        name: 'id',
        Cell: cell => (
          <View>
            { cell.canWrite &&
            <Button
              onPress={() => Actions.inchargesEdit({ inchargeId: cell.value })}
              iconName="pencil-square-o"
              iconColor="#FFF"
              buttonColor="#337ab7"
            >
                          Edit
            </Button>
                      }
          </View>
        ),
        hide: !this.props.selected,
      },
    ];
  }

  fetchData() {
    this.setState({ loading: true });
    const params = !this.props.selected ? this.state.templateParams : [
      ...this.state.templateParams,
      { name: 'selected', defaultValue: this.props.selected },
    ];
    this.props.fetchIncharges(buildSearchParams(params))
      .then(() => {
        this.setState({ loading: false, noDataMessage: 'No data Found' });
      })
      .catch(() => {
        this.setState({ loading: false, noDataMessage: 'Error occurred' });
      });
  }

  render() {
    return (
      <View style={{ flex: 1 }}>
        <Filters
          availableFilters={this.state.templateParams}
          onFilter={filters => this.onFilter(filters)}
          onReset={() => this.onReset()}
          iconTop={3}
          iconRight={3}
        />
        <Text style={[styles.title, lightThemeText]}>
          {this.props.title}
        </Text>
        { (this.props.incharges.length > 0 && !this.state.loading) &&
        <FlatList
          data={this.props.incharges}
          renderItem={
          ({ item }) =>
            (<ListItem
              row={item}
              columns={this.getColumnDefinitions()}
              canWrite={this.state.INCHARGE_WRITE_AUTHORITY}
            />)}
          keyExtractor={(item, index) => index}
        />
        }
        { (this.props.incharges.length === 0 && !this.state.loading) &&
          <Text style={styles.noDataMessage}>
            {this.state.noDataMessage}
          </Text>
        }
        { this.state.loading &&
          <View style={{ paddingTop: 60 }}>
            <Spinner />
          </View>
        }
      </View>
    );
  }
}

function mapStateToProps(state) {
  return {
    incharges: state.tablesReducer.inchargesList,
  };
}

export default connect(mapStateToProps, { fetchIncharges })(InchargeList);

InchargeList.propTypes = {
  fetchIncharges: PropTypes.func.isRequired,
  incharges: PropTypes.arrayOf(PropTypes.shape({
  })).isRequired,
  selected: PropTypes.bool,
  title: PropTypes.string.isRequired,
};

InchargeList.defaultProps = {
  selected: true,
};
