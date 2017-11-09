import React, { Component } from 'react';

import Header from './header';
import SideBar from './sidebar';
import Home from './home'

export default class App extends Component {
  render() {
    return (
        <div>
          <Header/>
          <div className="wrapper">
            <SideBar/>
            <div className="body-content">
              <Home/>
            </div>
          </div>
        </div>
    );
  }
}
