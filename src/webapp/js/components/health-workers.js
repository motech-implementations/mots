import React, { Component } from 'react';
import ChwTable from '../container/chw-table'

export default class HealthWorkers extends Component {

  render() {
    return (
        <div>
          <div>
            <h1>Community Health Workers</h1>
            <ChwTable/>
          </div>
        </div>
    );
  }
}
