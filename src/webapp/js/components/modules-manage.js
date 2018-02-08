import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { initialize, isDirty, getFormValues } from 'redux-form';
import SortableTree from 'react-sortable-tree';
import update from 'immutability-helper';

import apiClient from '../utils/api-client';
import { hasAuthority, MANAGE_MODULES_AUTHORITY } from '../utils/authorization';
import ModuleForm, { MODULE_FORM_NAME } from './modules-form';

class ModulesManage extends Component {
  static getNodeKey(node) {
    if (node.id !== undefined && node.id !== null) {
      return node.id;
    }

    return node.uiId;
  }

  static areEqual(nodeA, nodeB) {
    if (nodeA === null || nodeA === undefined) {
      return nodeB === null || nodeB === undefined;
    }

    return nodeB !== null && nodeB !== undefined && nodeA.type === nodeB.type
      && ModulesManage.getNodeKey(nodeA) === ModulesManage.getNodeKey(nodeB);
  }

  static isNodeNew(node) {
    return node.id === null || node.id === undefined;
  }

  static isNodeChanged(node) {
    return node.changed;
  }

  static findNodeIndex(array, id) {
    return _.findIndex(array, node => ModulesManage.getNodeKey(node) === id);
  }

  static findNode(array, id) {
    return _.find(array, node => ModulesManage.getNodeKey(node) === id);
  }

  constructor(props) {
    super(props);

    this.state = {
      selectedElement: {},
      treeData: [],
    };

    this.addModule = this.addModule.bind(this);
    this.saveModule = this.saveModule.bind(this);
    this.releaseModule = this.releaseModule.bind(this);
  }

  componentWillMount() {
    if (!hasAuthority(MANAGE_MODULES_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.fetchModules();
    }
  }

  getNodeClassName(node) {
    const classNames = [];

    if (node.type !== 'MODULE') {
      classNames.push('tree-node');
    } else if (node.status !== 'DRAFT') {
      classNames.push('tree-node-non-editable');
    } else if (ModulesManage.isNodeNew(node)) {
      classNames.push('tree-node-new');
    } else if (ModulesManage.isNodeChanged(node)) {
      classNames.push('tree-node-changed');
    } else {
      classNames.push('tree-node');
    }

    if (this.isNodeSelected(node)) {
      classNames.push('tree-node-selected');
    }

    return classNames.join(' ');
  }

  getNodeIndexPath(path) {
    const nodeIndexes = [];
    let nodeArray = this.state.treeData;

    path.forEach((nodeId) => {
      const nodeIndex = ModulesManage.findNodeIndex(nodeArray, nodeId);
      const node = nodeArray[nodeIndex];
      nodeArray = node.children;
      nodeIndexes.push(nodeIndex);
    });

    return nodeIndexes;
  }

  updateAndSelectModule(module, moduleIndex) {
    const newModule = { ...module, changed: false };

    this.setState(state => update(state, {
      treeData: {
        [moduleIndex]: { $merge: newModule },
      },
      selectedElement: { $set: { node: newModule, path: [newModule.id] } },
    }));

    this.props.initialize(MODULE_FORM_NAME, _.omit(newModule, 'children', 'expanded'));
  }

  saveModule() {
    const moduleId = this.state.selectedElement.path[0];
    const moduleIndex = ModulesManage.findNodeIndex(this.state.treeData, moduleId);
    const module = { ...this.state.treeData[moduleIndex], ...this.props.formValues };

    if (ModulesManage.isNodeNew(module)) {
      apiClient.post('/api/modules', module)
        .then((response) => {
          this.updateAndSelectModule(response.data, moduleIndex);
        });
    } else {
      apiClient.put(`/api/modules/${module.id}`, module)
        .then((response) => {
          this.updateAndSelectModule(response.data, moduleIndex);
        });
    }
  }

  releaseModule() {
    const moduleId = this.state.selectedElement.path[0];
    const moduleIndex = ModulesManage.findNodeIndex(this.state.treeData, moduleId);

    apiClient.put(`/api/modules/${moduleId}/release`)
      .then((response) => {
        this.updateAndSelectModule(response.data, moduleIndex);
      });
  }

  fetchModules() {
    const url = '/api/modules';

    apiClient.get(url)
      .then((response) => {
        const treeData = response.data;

        this.setState({ treeData });
      });
  }

  addModule() {
    const newModule = {
      uiId: _.uniqueId(),
      type: 'MODULE',
      status: 'DRAFT',
      children: [],
      startModuleQuestion: { type: 'QUESTION' },
      expanded: true,
    };

    this.setState(state => update(state, {
      treeData: { $push: [newModule] },
      selectedElement: { $set: { node: newModule, path: [newModule.uiId] } },
    }));

    this.props.initialize(MODULE_FORM_NAME, _.omit(newModule, 'children', 'expanded'));
  }

  updateModule(nodeIndexPath, newModule) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: { $merge: { ...newModule, changed: true } },
      }),
    }));
  }

  updateUnit(nodeIndexPath, newUnit) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: {
            [nodeIndexPath[1]]: { $merge: newUnit },
          },
        },
      }),
    }));
  }

  updateMessageOrQuestion(nodeIndexPath, newNode) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: {
            [nodeIndexPath[1]]: {
              children: {
                [nodeIndexPath[2]]: { $merge: newNode },
              },
            },
          },
        },
      }),
    }));
  }

  updateNode(path, newNode) {
    const nodeIndexPath = this.getNodeIndexPath(path);

    switch (newNode.type) {
      case 'MODULE':
        this.updateModule(nodeIndexPath, newNode);
        break;
      case 'UNIT':
        this.updateUnit(nodeIndexPath, newNode);
        break;
      default:
        this.updateMessageOrQuestion(nodeIndexPath, newNode);
    }
  }

  reinitializeForm(module) {
    if (ModulesManage.areEqual(this.state.selectedElement.node, module)) {
      this.props.initialize(MODULE_FORM_NAME, _.omit({ ...module, changed: true }, 'children', 'expanded'), true);
    }
  }

  addUnit(path) {
    const nodeIndexPath = this.getNodeIndexPath(path);

    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: {
            $push: [{
              uiId: _.uniqueId(),
              type: 'UNIT',
              allowReplay: false,
              children: [],
              unitContinuationQuestion: { type: 'QUESTION' },
              expanded: true,
            }],
          },
        },
      }),
    }));

    this.reinitializeForm(this.state.treeData[nodeIndexPath[0]]);
  }

  addMessageOrQuestion(path, type) {
    const nodeIndexPath = this.getNodeIndexPath(path);

    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: {
            [nodeIndexPath[1]]: {
              children: { $push: [{ uiId: _.uniqueId(), type }] },
            },
          },
        },
      }),
    }));

    this.reinitializeForm(this.state.treeData[nodeIndexPath[0]]);
  }

  addMessage(path) {
    this.addMessageOrQuestion(path, 'MESSAGE');
  }

  addQuestion(path) {
    this.addMessageOrQuestion(path, 'QUESTION');
  }

  removeUnit(nodeIndexPath) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: { $splice: [[nodeIndexPath[1], 1]] },
        },
      }),
    }));

    this.reinitializeForm(this.state.treeData[nodeIndexPath[0]]);
  }

  removeMessageOrQuestion(nodeIndexPath) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: {
            [nodeIndexPath[1]]: {
              children: { $splice: [[nodeIndexPath[2], 1]] },
            },
          },
        },
      }),
    }));

    this.reinitializeForm(this.state.treeData[nodeIndexPath[0]]);
  }

  removeNode(path, node) {
    const nodeIndexPath = this.getNodeIndexPath(path);

    if (node.type === 'UNIT') {
      this.removeUnit(nodeIndexPath);
    } else {
      this.removeMessageOrQuestion(nodeIndexPath);
    }

    this.selectParentNode(path, nodeIndexPath, node);
  }

  findNodeByIndex(nodeIndexPath) {
    let nodeArray = this.state.treeData;
    let node = null;

    nodeIndexPath.forEach((nodeIndex) => {
      node = nodeArray[nodeIndex];
      nodeArray = node.children;
    });

    return node;
  }

  selectParentNode(path, nodeIndexPath, node) {
    const lastIndex = path.length - 1;

    if (this.isNodeSelected(node) && lastIndex > 0) {
      const newPath = path.slice(0, lastIndex);
      const newIndexPath = nodeIndexPath.slice(0, lastIndex);
      const newNode = this.findNodeByIndex(newIndexPath);

      this.setState(() => ({
        selectedElement: { node: newNode, path: newPath },
      }));

      this.props.initialize(MODULE_FORM_NAME, _.omit(newNode, 'children', 'expanded'));
    }
  }

  isNodeSelected(node) {
    return ModulesManage.areEqual(node, this.state.selectedElement.node);
  }

  isEditable(node, path) {
    const module = node.type === 'MODULE' ? node : ModulesManage.findNode(this.state.treeData, path[0]);
    return !module || module.status === 'DRAFT';
  }

  render() {
    const canDrop = ({ node, prevPath, nextPath }) => (node.type === 'MODULE'
      && nextPath.length === 1) || (prevPath.length === nextPath.length
      && prevPath[prevPath.length - 2] === nextPath[nextPath.length - 2]);
    const canDrag = ({ node, path }) => node.type !== 'MODULE' && this.isEditable(node, path);

    const onMoveNode = ({ node, prevPath }) => {
      if (node.type !== 'MODULE') {
        const moduleIndex = ModulesManage.findNodeIndex(this.state.treeData, prevPath[0]);
        this.setState(state => ({
          treeData: update(state.treeData, {
            [moduleIndex]: { changed: { $set: true } },
          }),
        }));

        this.reinitializeForm(this.state.treeData[moduleIndex]);
      }
    };

    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">Manage Modules</h1>
        <button
          className="btn btn-success margin-bottom-lg"
          onClick={this.addModule}
        >
          <span className="glyphicon glyphicon-plus" />
          <span className="icon-text">Add Module</span>
        </button>
        <div className="row">
          <div className="col-md-6 .tree-container">
            <SortableTree
              treeData={this.state.treeData}
              onChange={treeData => this.setState({ treeData })}
              getNodeKey={({ node }) => ModulesManage.getNodeKey(node)}
              isVirtualized={false}
              generateNodeProps={({ node, path }) => ({
                title: node.name,
                onClick: () => {
                  if (this.state.selectedElement.path && this.props.formDirty) {
                    this.updateNode(this.state.selectedElement.path, this.props.formValues);
                  }

                  this.props.initialize(MODULE_FORM_NAME, _.omit(node, 'children', 'expanded'));
                  this.setState(() => ({
                    selectedElement: { node, path },
                  }));
                },
                className: this.getNodeClassName(node),
                buttons: node.type === 'MODULE' || !this.isEditable(node, path) ? [] : [
                  <button
                    className="btn btn-danger"
                    onClick={(event) => {
                      this.removeNode(path, node);
                      event.stopPropagation();
                    }}
                  >
                    <span className="glyphicon glyphicon-trash" />
                  </button>,
                ],
              })}
              canDrop={canDrop}
              canDrag={canDrag}
              onMoveNode={onMoveNode}
            />
          </div>
          <div className="col-md-6">
            {
              this.state.selectedElement.path && <ModuleForm
                onSubmit={this.saveModule}
                releaseModule={this.releaseModule}
                isEditable={this.isEditable(
                  this.state.selectedElement.node,
                  this.state.selectedElement.path,
                )}
                addUnit={() => this.addUnit(this.state.selectedElement.path)}
                addMessage={() => this.addMessage(this.state.selectedElement.path)}
                addQuestion={() => this.addQuestion(this.state.selectedElement.path)}
              />
            }
          </div>
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    formValues: getFormValues(MODULE_FORM_NAME)(state),
    formDirty: isDirty(MODULE_FORM_NAME)(state),
  };
}

export default connect(mapStateToProps, { initialize })(ModulesManage);

ModulesManage.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  initialize: PropTypes.func.isRequired,
  formDirty: PropTypes.bool,
  formValues: PropTypes.shape({}),
};

ModulesManage.defaultProps = {
  formDirty: false,
  formValues: {},
};
