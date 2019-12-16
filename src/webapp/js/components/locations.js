import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';

import 'react-tabs/style/react-tabs.scss';

import { resetLogoutCounter } from '../actions/index';
import LocationsTable from '../container/locations-table';
import {
  FETCH_SECTORS, FETCH_DISTRICTS, FETCH_FACILITIES,
  FETCH_VILLAGES,
} from '../actions/types';
import {
  hasAuthority,
  canEditLocation,
  MANAGE_FACILITIES_AUTHORITY,
  MANAGE_OWN_FACILITIES_AUTHORITY,
} from '../utils/authorization';

class Locations extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedIndex: 0,
    };

    this.handleTabSelect = this.handleTabSelect.bind(this);
  }

  componentWillMount() {
    if (this.props.match.params.tabIndex) {
      this.setState({ selectedIndex: Number.parseInt(this.props.match.params.tabIndex, 10) });
    }
  }

  static getVillageColumns = () => [
    {
      Header: 'Actions',
      minWidth: 50,
      accessor: 'id',
      Cell: cellInfo => (
        <div className="actions-buttons-container">
          { canEditLocation(cellInfo.original) &&
          <Link
            to={`/locations/village/${cellInfo.value}`}
            type="button"
            className="btn btn-primary margin-right-sm"
            title="Edit"
          >
            <span className="glyphicon glyphicon-edit" />
            <span className="hide-min-r-small-min next-button-text">Edit</span>
          </Link>
          }
        </div>
      ),
      filterable: false,
      sortable: false,
      show: hasAuthority(MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY),
    },
    {
      Header: 'Name',
      accessor: 'name',
    }, {
      Header: 'Facility',
      accessor: 'parent',
    }, {
      Header: 'Sector',
      accessor: 'sectorName',
    }, {
      Header: 'District',
      accessor: 'districtName',
    },
  ];

  static getFacilityColumns = () => [
    {
      Header: 'Actions',
      minWidth: 50,
      accessor: 'id',
      Cell: cellInfo => (
        <div className="actions-buttons-container">
          { canEditLocation(cellInfo.original) &&
          <Link
            to={`/locations/facility/${cellInfo.value}`}
            type="button"
            className="btn btn-primary margin-right-sm"
            title="Edit"
          >
            <span className="glyphicon glyphicon-edit" />
            <span className="hide-min-r-small-min next-button-text">Edit</span>
          </Link>
          }
        </div>
      ),
      filterable: false,
      sortable: false,
      show: hasAuthority(MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY),
    },
    {
      Header: 'Name',
      accessor: 'name',
    }, {
      Header: 'Incharge name',
      accessor: 'inchargeFullName',
    }, {
      Header: 'Incharge phone',
      accessor: 'inchargePhone',
    }, {
      Header: 'Incharge email',
      accessor: 'inchargeEmail',
    }, {
      Header: 'Sector',
      accessor: 'parent',
    }, {
      Header: 'District',
      accessor: 'districtName',
    },
  ];

  static getSectorColumns = () => [
    {
      Header: 'Actions',
      minWidth: 50,
      accessor: 'id',
      Cell: cellInfo => (
        <div className="actions-buttons-container">
          {canEditLocation(cellInfo.original) &&
            <Link
              to={`/locations/sector/${cellInfo.value}`}
              type="button"
              className="btn btn-primary margin-right-sm"
              title="Edit"
            >
              <span className="glyphicon glyphicon-edit" />
              <span className="hide-min-r-small-min next-button-text">Edit</span>
            </Link>
          }
        </div>
      ),
      filterable: false,
      sortable: false,
      show: hasAuthority(MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY),
    },
    {
      Header: 'Name',
      accessor: 'name',
    }, {
      Header: 'District',
      accessor: 'parent',
    },
  ];

  static getDistrictColumns = () => [
    {
      Header: 'Actions',
      minWidth: 50,
      accessor: 'id',
      Cell: cellInfo => (
        <div className="actions-buttons-container">
          {canEditLocation(cellInfo.original) &&
            <Link
              to={`/locations/district/${cellInfo.value}`}
              type="button"
              className="btn btn-primary margin-right-sm"
              title="Edit"
            >
              <span className="glyphicon glyphicon-edit" />
              <span className="hide-min-r-small-min next-button-text">Edit</span>
            </Link>
          }
        </div>
      ),
      filterable: false,
      sortable: false,
      show: hasAuthority(MANAGE_FACILITIES_AUTHORITY, MANAGE_OWN_FACILITIES_AUTHORITY),
    },
    {
      Header: 'Name',
      accessor: 'name',
    },
  ];

  handleTabSelect(index) {
    this.setState({ selectedIndex: index });
    this.props.resetLogoutCounter();
  }

  render() {
    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Locations</h1>
        <Tabs selectedIndex={this.state.selectedIndex} onSelect={this.handleTabSelect}>
          <TabList>
            <Tab>Villages</Tab>
            <Tab>Facilities</Tab>
            <Tab>Sectors</Tab>
            <Tab>Districts</Tab>
          </TabList>

          <TabPanel>
            <LocationsTable
              locationType={FETCH_VILLAGES}
              tableColumns={Locations.getVillageColumns()}
            />
          </TabPanel>
          <TabPanel>
            <LocationsTable
              locationType={FETCH_FACILITIES}
              tableColumns={Locations.getFacilityColumns()}
            />
          </TabPanel>
          <TabPanel>
            <LocationsTable
              locationType={FETCH_SECTORS}
              tableColumns={Locations.getSectorColumns()}
            />
          </TabPanel>
          <TabPanel>
            <LocationsTable
              locationType={FETCH_DISTRICTS}
              tableColumns={Locations.getDistrictColumns()}
            />
          </TabPanel>
        </Tabs>
      </div>
    );
  }
}

export default connect(null, { resetLogoutCounter })(Locations);

Locations.propTypes = {
  match: PropTypes.shape({
    params: PropTypes.shape({
      tabIndex: PropTypes.string,
    }),
  }).isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
};
