// TODO: Uncomment all lines below, when API actions will be properly implemented
// TODO: Remove temporary mock data and remove state from InchargeList
import React, { Component } from 'react';
import { ScrollView, Text, TouchableOpacity } from 'react-native';
import { Actions } from 'react-native-router-flux';
// import { connect } from 'react-redux';
// import PropTypes from 'prop-types';
import Icon from 'react-native-vector-icons/FontAwesome';

import ListItems from '../components/ListItems';
// import { fetchIncharges } from '../actions/index';

const styles = {
  actionButton: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    width: 60,
    borderRadius: 3,
    backgroundColor: '#337ab7',
    paddingHorizontal: 10,
    paddingVertical: 5,
  },
  buttonLabel: {
    marginLeft: 5,
    color: '#FFF',
  },
};

const COLUMNS = [
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
    Cell: () => (
      <TouchableOpacity
        onPress={Actions.menu}
        style={styles.actionButton}
      >
        <Icon name="pencil-square-o" color="#FFF" size={16} />
        <Text style={styles.buttonLabel}>Edit</Text>
      </TouchableOpacity>
    ),
  },
];


class InchargeList extends Component {
  state = { incharges: TEMPORARY_MOCK_DATA };

  // componentWillMount() {
  //   this.props.fetchIncharges();
  // }

  render() {
    return (
      <ScrollView style={{ marginBottom: 40 }}>
        <ListItems data={this.state.incharges} columns={COLUMNS} />
      </ScrollView>
    );
  }
}

// function mapStateToProps(state) {
//   return {
//     incharges: state.tablesReducer.inchargesList,
//   };
// }

// export default connect(mapStateToProps, { fetchIncharges })(InchargeList);
export default InchargeList;

// InchargeList.propTypes = {
//   fetchIncharges: PropTypes.func.isRequired,
//   incharges: PropTypes.arrayOf(PropTypes.shape({
//   })).isRequired,
// };

const TEMPORARY_MOCK_DATA = [
  {
    id: 'id1',
    firstName: 'John',
    secondName: 'Doe',
    otherName: null,
    phoneNumber: '2312313123',
    email: null,
    districtId: 'districtId-1',
    chiefdomId: 'chiefdomId-1',
    facilityId: 'facilityId-1',
    facilityName: 'Shekaia MCHP',
  },
  {
    id: 'id2',
    firstName: 'Jane',
    secondName: 'Doe',
    otherName: null,
    phoneNumber: '123333333',
    email: null,
    districtId: 'districtId-2',
    chiefdomId: 'chiefdomId-2',
    facilityId: 'facilityId-2',
    facilityName: 'Tombo Wallah CHP',
  },
  {
    id: 'id3',
    firstName: 'Firstname',
    secondName: 'LastName',
    otherName: null,
    phoneNumber: '123123123',
    email: null,
    districtId: 'districtId-3',
    chiefdomId: 'chiefdomId-3',
    facilityId: 'facilityId-3',
    facilityName: 'Gbolon MCHP',
  },
];
