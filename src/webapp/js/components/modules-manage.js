import React, { Component } from 'react';
import PropTypes from 'prop-types';
import SortableTree from 'react-sortable-tree';

import apiClient from '../utils/api-client';
import { hasAuthority, MANAGE_MODULES_AUTHORITY } from '../utils/authorization';

export default class ModulesManage extends Component {
  constructor(props) {
    super(props);

    this.state = {
      treeData: [],
    };
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_MODULES_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.fetchModules();
    }
  }

  fetchModules() {
    const url = '/api/modules';

    apiClient.get(url)
      .then((response) => {
        const treeData = response.data;

        this.setState({ treeData });
      });
  }

  render() {
    const canDrop = ({ prevParent, nextParent }) => {
      if (prevParent === null || prevParent === undefined) {
        return nextParent === null || nextParent === undefined;
      }

      return nextParent !== null && nextParent !== undefined
        && prevParent.type === nextParent.type && prevParent.id === nextParent.id;
    };

    return (
      <div style={{ height: 1000 }}>
        <SortableTree
          treeData={this.state.treeData}
          onChange={treeData => this.setState({ treeData })}
          generateNodeProps={({ node }) => ({
            title: node.name,
          })}
          canDrop={canDrop}
        />
      </div>
    );
  }
}

ModulesManage.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
};
