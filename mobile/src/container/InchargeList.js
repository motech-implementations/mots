import React, { Component } from 'react';
import { FlatList, View } from 'react-native';
import { Actions } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import Button from '../components/Button';

import ListItem from '../components/ListItem';
import { fetchIncharges } from '../actions/index';
import { INCHARGE_WRITE_AUTHORITY, hasAuthority } from '../utils/authorization';

class InchargeList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      INCHARGE_WRITE_AUTHORITY: false,
    };
  }

  componentWillMount() {
    hasAuthority(INCHARGE_WRITE_AUTHORITY).then((result) => {
      if (result) { this.setState({ INCHARGE_WRITE_AUTHORITY: true }); }
    });
    this.props.fetchIncharges(this.props.selected);
  }

  getColumnDefinitions() {
    return [
      {
        Header: 'First name',
        accessor: 'firstName',
      }, {
        Header: 'Surname',
        accessor: 'secondName',
      }, {
        Header: 'Other name',
        accessor: 'otherName',
      }, {
        Header: 'Phone number',
        accessor: 'phoneNumber',
      }, {
        Header: 'Email',
        accessor: 'email',
      }, {
        Header: 'Facility',
        accessor: 'facilityName',
      },
      {
        Header: 'Actions',
        minWidth: 50,
        accessor: 'id',
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

  render() {
    return (
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
};

InchargeList.defaultProps = {
  selected: true,
};
