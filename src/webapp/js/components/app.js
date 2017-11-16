import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';

import Header from './header';
import SideBar from './sidebar';
import Home from './home';
import HealthWorkers from './health-workers';
import HealthWorkersNew from './health-workers-new';

export default class App extends Component {
  render() {
    return (
        <div>
          <Header/>
          <div className="wrapper">
            <SideBar/>
            <div className="body-content">
              <Switch>
                <Route path="/chw/new" component={ HealthWorkersNew } />
                <Route path="/chw" component={ HealthWorkers } />
                <Route path="/" component={ Home } />
              </Switch>
            </div>
          </div>
        </div>
    );
  }
}
