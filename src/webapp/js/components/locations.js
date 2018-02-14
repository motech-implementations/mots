import React from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';

import 'react-tabs/style/react-tabs.scss';
import CommunitiesTable from '../container/communities-table';
import FacilitiesTable from '../container/facilities-table';
import ChiefdomsTable from '../container/chiefdoms-table';
import DistrictsTable from '../container/districts-table';

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
        <CommunitiesTable />
      </TabPanel>
      <TabPanel>
        <FacilitiesTable />
      </TabPanel>
      <TabPanel>
        <ChiefdomsTable />
      </TabPanel>
      <TabPanel>
        <DistrictsTable />
      </TabPanel>
    </Tabs>
  </div>
);

export default Locations;
