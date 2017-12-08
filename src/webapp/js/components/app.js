import React, { Component } from 'react';
import { Route, Switch } from 'react-router-dom';
import Alert from 'react-s-alert';

import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/bouncyflip.css';

import Navbar from './navbar';
import Home from './home';
import HealthWorkers from './health-workers';
import HealthWorkersNew from './health-workers-new';
import HealthWorkersEdit from './health-workers-edit';
import AssignModules from './assign-modules';

export default class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showMenuSmart: false,
    };

    this.toggleShowMenuSmart = this.toggleShowMenuSmart.bind(this);
    this.hideMenuSmart = this.hideMenuSmart.bind(this);
  }

  toggleShowMenuSmart() {
    this.setState({
      showMenuSmart: !this.state.showMenuSmart,
    });
  }

  hideMenuSmart() {
    this.setState({
      showMenuSmart: false,
    });
  }

  render() {
    return (
      <div id="wrapper">
        <Navbar
          showMenuSmart={this.state.showMenuSmart}
          toggleShowMenuSmart={this.toggleShowMenuSmart}
          hideMenuSmart={this.hideMenuSmart}
        />
        <div id="page-wrapper" className={this.state.showMenuSmart ? 'hide-max-r-xsmall-max' : ''}>
          <div className="container-fluid">
            <Switch>
              <Route path="/modules/assign/:chwId" component={AssignModules} />
              <Route path="/chw/new" component={HealthWorkersNew} />
              <Route path="/chw/:chwId" component={HealthWorkersEdit} />
              <Route path="/chw" component={HealthWorkers} />
              <Route path="/" component={Home} />
            </Switch>
          </div>
        </div>

        <Alert
          timeout={5000}
          stack={{ limit: 3 }}
          offset={32}
          html
          position="top-right"
          effect="bouncyflip"
        />
      </div>
    );
  }
}
