import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import Alert from 'react-s-alert';

import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/bouncyflip.css';

import Header from './header';
import SideBar from './sidebar';
import Home from './home';
import HealthWorkers from './health-workers';
import HealthWorkersNew from './health-workers-new';
import AssignModules from './assign-modules'


export default class App extends Component {
  render() {
    return (
        <div>
          <Header/>
          <div className="wrapper">
            <SideBar/>
            <div className="body-content">
              <Switch>
                <Route path="/modules/assign/:chwId" component={ AssignModules } />
                <Route path="/chw/new" component={ HealthWorkersNew } />
                <Route path="/chw" component={ HealthWorkers } />
                <Route path="/" component={ Home } />
              </Switch>
            </div>
          </div>
          <Alert timeout={5000}
                 stack={{limit: 3}}
                 offset={32}
                 html={true}
                 position='top-right'
                 effect='bouncyflip'/>
        </div>
    );
  }
}
