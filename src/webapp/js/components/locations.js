import React, { Component } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';

import 'react-tabs/style/react-tabs.scss';
import LocationsTable from '../container/locations-table';
import {
  FETCH_CHIEFDOMS, FETCH_DISTRICTS, FETCH_FACILITIES,
  FETCH_COMMUNITIES,
} from '../actions/types';

const DISTRICT_COLUMNS = [
  {
    Header: 'Name',
    accessor: 'name',
  },
];

const CHIEFDOM_COLUMNS = [
  {
    Header: 'Name',
    accessor: 'name',
  }, {
    Header: 'District',
    accessor: 'parent',
  },
];

class FACILITIES_COLUMNS extends Component {
  static getColumns = () => [
    {
      Header: 'Facility ID',
      accessor: 'facilityId',
    }, {
      Header: 'Name',
      accessor: 'name',
    }, {
      Header: 'Facility Type',
      accessor: 'facilityType',
      Filter: ({ onChange }) => (
        <select
          onChange={event => onChange(event.target.value)}
          style={{ width: '100%' }}
        >
          <option value="">Show All</option>
          <option value="CHC">CHC</option>
          <option value="CHP">CHP</option>
          <option value="MCHP">MCHP</option>
          <option value="clinic">Clinic</option>
          <option value="hospital">Hospital</option>
        </select>),
    }, {
      Header: 'Incharge name',
      accessor: 'inchargeFullName',
    }, {
      Header: 'Chiefdom',
      accessor: 'parent',
    },
  ];
}

const COMMUNITY_COLUMNS = [
  {
    Header: 'Name',
    accessor: 'name',
  }, {
    Header: 'Facility',
    accessor: 'parent',
  },
];

const Locations = () => (
  <div>
    <h1 className="page-header padding-bottom-xs margin-x-sm">Locations</h1>
    <Tabs>
      <TabList>
        <Tab>Communities</Tab>
        <Tab>Facilities</Tab>
        <Tab>Chiefdoms</Tab>
        <Tab>Districts</Tab>
      </TabList>

      <TabPanel>
        <LocationsTable locationType={FETCH_COMMUNITIES} tableColumns={COMMUNITY_COLUMNS} />
      </TabPanel>
      <TabPanel>
        <LocationsTable
          locationType={FETCH_FACILITIES}
          tableColumns={FACILITIES_COLUMNS.getColumns()}
        />
      </TabPanel>
      <TabPanel>
        <LocationsTable locationType={FETCH_CHIEFDOMS} tableColumns={CHIEFDOM_COLUMNS} />
      </TabPanel>
      <TabPanel>
        <LocationsTable locationType={FETCH_DISTRICTS} tableColumns={DISTRICT_COLUMNS} />
      </TabPanel>
    </Tabs>
  </div>
);

export default Locations;
