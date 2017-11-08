import ReactDOM from 'react-dom';
import React, { Component } from 'react';
import Header from './components/header';
import SideBar from './components/sidebar';
import Home from './components/home'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css';
import '../css/main.scss';


class App extends Component {
    render() {
        return (
            <div>
                <Header />
              <div className="wrapper">
                <SideBar />
                <div className="body-content">
                  <Home />
                </div>
              </div>
            </div>
        );
    }
}

ReactDOM.render(
    <App />, document.getElementById('root')
);