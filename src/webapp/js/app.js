import ReactDOM from 'react-dom';
import React, { Component } from 'react';

export default class App extends Component {
    render() {
        return (
            <div>REACT POWER</div>
        );
    }
}

ReactDOM.render(
    <App />, document.getElementById('root')
);