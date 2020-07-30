import _ from 'lodash';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { initialize, isDirty, getFormValues } from 'redux-form';
import SortableTree from 'react-sortable-tree';
import update from 'immutability-helper';
import { toast } from 'react-toastify';

import 'react-sortable-tree/style.css';

import apiClient from '../utils/api-client';
import {
  DISPLAY_MODULES_AUTHORITY, hasAuthority,
  MANAGE_MODULES_AUTHORITY,
} from '../utils/authorization';
import ModuleForm, { MODULE_FORM_NAME } from './modules-form';
import MotsConfirmModal from './mots-confirm-modal';
import { resetLogoutCounter } from '../actions/index';

class ModulesManage extends Component {
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

  static showSaveSuccessMessage(type) {
    let nodeName = 'Module';

    if (type === 'courses') {
      nodeName = 'Course';
    }

    toast.success(`${nodeName} has been saved successfully`);
  }

  constructor(props) {
    super(props);

    this.state = {
      selectedElement: {},
      treeData: [],
      canAddCourse: true,
      showConfirmModal: false,
    };

    this.addCourse = this.addCourse.bind(this);
    this.saveNode = this.saveNode.bind(this);
    this.releaseCourse = this.releaseCourse.bind(this);
    this.editModule = this.editModule.bind(this);
    this.showConfirmModal = this.showConfirmModal.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
  }

  componentDidMount() {
    if (!hasAuthority(MANAGE_MODULES_AUTHORITY, DISPLAY_MODULES_AUTHORITY)) {
      this.props.history.push('/home');
    } else {
      this.fetchCourses();
    }
  }

  componentDidUpdate() {
    this.canAddCourse(this.state.treeData);
  }

  static getNodeKey(node) {
    if (node.treeId !== undefined && node.treeId !== null) {
      return node.treeId;
    }

    return node.uiId;
  }

  static getNodeTypeForUrl(node) {
    switch (node.type) {
      case 'COURSE':
        return 'courses';
      default:
        return 'modules';
    }
  }

  getNodeClassName(node) {
    const classNames = [];

    if (node.type !== 'COURSE' && node.type !== 'MODULE') {
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

  getNodeWithFormValues(nodeIndexPath) {
    switch (this.state.selectedElement.node.type) {
      case 'COURSE':
        return { ...this.state.treeData[nodeIndexPath[0]], ...this.props.formValues };
      default:
        return {
          ...this.state.treeData[nodeIndexPath[0]].children[nodeIndexPath[1]],
          ...this.props.formValues,
        };
    }
  }

  updateAndSelectModule(module, moduleIndexPath) {
    const newModule = { ...module, changed: false };

    this.setState(state => update(state, {
      treeData: {
        [moduleIndexPath[0]]: {
          children: {
            [moduleIndexPath[1]]: { $merge: newModule },
          },
        },
      },
      selectedElement: {
        $set: {
          node: newModule,
          path: [state.treeData[moduleIndexPath[0]].treeId, newModule.treeId],
        },
      },
    }));

    this.props.initialize(MODULE_FORM_NAME, _.omit(newModule, 'children', 'expanded'));
  }

  updateAndSelectCourse(course, courseIndex) {
    const newCourse = { ...course, changed: false };

    this.setState(state => update(state, {
      treeData: {
        [courseIndex]: { $merge: newCourse },
      },
      selectedElement: { $set: { node: newCourse, path: [newCourse.treeId] } },
    }));

    this.props.initialize(MODULE_FORM_NAME, _.omit(newCourse, 'children', 'expanded'));
  }

  addAndSelectCourse(course) {
    this.setState(state => update(state, {
      treeData: {
        $push: [course],
      },
      selectedElement: { $set: { node: course, path: [course.treeId] } },
    }));

    this.props.initialize(MODULE_FORM_NAME, _.omit(course, 'children', 'expanded'));
  }

  updateAndSelectNode(node, nodeIndexPath) {
    const newNode = { ...node, changed: false };

    switch (newNode.type) {
      case 'COURSE':
        this.updateAndSelectCourse(node, nodeIndexPath);
        break;
      default:
        this.updateAndSelectModule(node, nodeIndexPath);
    }
  }

  saveNode() {
    const nodeIndexPath = this.getNodeIndexPath(this.state.selectedElement.path);
    const type = ModulesManage.getNodeTypeForUrl(this.state.selectedElement.node);
    const node = this.getNodeWithFormValues(nodeIndexPath);

    if (ModulesManage.isNodeNew(node)) {
      apiClient.post(`/api/${type}`, node)
        .then((response) => {
          this.updateAndSelectNode(response.data, nodeIndexPath);
          ModulesManage.showSaveSuccessMessage(type);
        });
    } else {
      apiClient.put(`/api/${type}/${node.id}`, node)
        .then((response) => {
          this.updateAndSelectNode(response.data, nodeIndexPath);
          ModulesManage.showSaveSuccessMessage(type);
        });
    }
  }

  releaseCourse() {
    const courseId = this.state.selectedElement.path[0];
    const courseIndex = ModulesManage.findNodeIndex(this.state.treeData, courseId);

    apiClient.put(`/api/courses/${courseId}/release`)
      .then((response) => {
        this.updateAndSelectCourse(response.data, courseIndex);
        toast.success('Course has been published successfully');
      });
  }

  editModule() {
    const moduleId = this.state.selectedElement.node.id;
    const nodeIndexPath = this.getNodeIndexPath(this.state.selectedElement.path);

    apiClient.put(`/api/modules/${moduleId}/edit`)
      .then((response) => {
        this.updateAndSelectModule(response.data, nodeIndexPath);
        toast.success('New module draft has been created successfully');
      });

    this.hideConfirmModal();
  }

  hideConfirmModal() {
    this.setState({ showConfirmModal: false });
  }

  showConfirmModal() {
    this.setState({ showConfirmModal: true });
  }

  fetchCourses() {
    const url = '/api/courses';

    apiClient.get(url)
      .then((response) => {
        const treeData = response.data;

        this.canAddCourse(treeData);
        this.setState({ treeData });
      });
  }

  updateCourse(nodeIndexPath, newCourse) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: { $merge: { ...newCourse, changed: true } },
      }),
    }));
  }

  updateModule(nodeIndexPath, newModule) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: {
            [nodeIndexPath[1]]: { $merge: { ...newModule, changed: true } },
          },
        },
      }),
    }));
  }

  updateUnit(nodeIndexPath, newUnit) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: {
            [nodeIndexPath[1]]: {
              changed: { $set: true },
              children: {
                [nodeIndexPath[2]]: { $merge: newUnit },
              },
            },
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
              changed: { $set: true },
              children: {
                [nodeIndexPath[2]]: {
                  children: {
                    [nodeIndexPath[3]]: { $merge: newNode },
                  },
                },
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
      case 'COURSE':
        this.updateCourse(nodeIndexPath, newNode);
        break;
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

  reinitializeFormForSelectedNode(node) {
    const selectedNode = this.state.selectedElement.node;

    if (ModulesManage.areEqual(selectedNode, node)) {
      this.props.initialize(MODULE_FORM_NAME, _.omit({ ...node, changed: true }, 'children', 'expanded'), true);
    }
  }

  reinitializeForm(nodeIndexPath) {
    const selectedNode = this.state.selectedElement.node;
    const course = this.state.treeData[nodeIndexPath[0]];

    if (selectedNode.type === 'COURSE') {
      this.reinitializeFormForSelectedNode(course);
    } else if (selectedNode.type === 'MODULE' && course) {
      const module = course.children[nodeIndexPath[1]];

      this.reinitializeFormForSelectedNode(module);
    }

    this.props.resetLogoutCounter();
  }

  addCourse() {
    apiClient.post('/api/courses')
      .then((response) => {
        this.addAndSelectCourse(response.data);
        toast.success('New Course has been created successfully');
      });
  }

  addModule(path) {
    const nodeIndexPath = this.getNodeIndexPath(path);

    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          expanded: { $set: true },
          children: {
            $push: [{
              uiId: _.uniqueId(),
              type: 'MODULE',
              status: 'DRAFT',
              children: [],
              expanded: true,
            }],
          },
        },
      }),
    }));

    this.reinitializeForm(nodeIndexPath);
  }

  addUnit(path) {
    const nodeIndexPath = this.getNodeIndexPath(path);

    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          expanded: { $set: true },
          children: {
            [nodeIndexPath[1]]: {
              changed: { $set: true },
              expanded: { $set: true },
              children: {
                $push: [{
                  uiId: _.uniqueId(),
                  type: 'UNIT',
                  allowReplay: false,
                  children: [],
                  expanded: true,
                }],
              },
            },
          },
        },
      }),
    }));

    this.reinitializeForm(nodeIndexPath);
  }

  addMessageOrQuestion(path, type) {
    const nodeIndexPath = this.getNodeIndexPath(path);

    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          expanded: { $set: true },
          children: {
            [nodeIndexPath[1]]: {
              changed: { $set: true },
              expanded: { $set: true },
              children: {
                [nodeIndexPath[2]]: {
                  expanded: { $set: true },
                  children: {
                    $push: [{ uiId: _.uniqueId(), type }],
                  },
                },
              },
            },
          },
        },
      }),
    }));

    this.reinitializeForm(nodeIndexPath);
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
          children: {
            [nodeIndexPath[1]]: {
              changed: { $set: true },
              children: { $splice: [[nodeIndexPath[2], 1]] },
            },
          },
        },
      }),
    }));
  }

  removeMessageOrQuestion(nodeIndexPath) {
    this.setState(state => ({
      treeData: update(state.treeData, {
        [nodeIndexPath[0]]: {
          changed: { $set: true },
          children: {
            [nodeIndexPath[1]]: {
              changed: { $set: true },
              children: {
                [nodeIndexPath[2]]: {
                  children: { $splice: [[nodeIndexPath[3], 1]] },
                },
              },
            },
          },
        },
      }),
    }));
  }

  removeNode(path, node) {
    const nodeIndexPath = this.getNodeIndexPath(path);

    if (node.type === 'UNIT') {
      this.removeUnit(nodeIndexPath);
    } else {
      this.removeMessageOrQuestion(nodeIndexPath);
    }

    this.reinitializeForm(nodeIndexPath);
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
      newNode.changed = true;

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
    if (!hasAuthority(MANAGE_MODULES_AUTHORITY)) {
      return false;
    }

    if (node.type === 'COURSE') {
      return node.status === 'DRAFT';
    }

    const course = ModulesManage.findNode(this.state.treeData, path[0]);

    return course && course.status === 'DRAFT';
  }

  isDraftCourse(node, path) {
    if (node.type === 'COURSE') {
      return node.status === 'DRAFT';
    }

    const course = ModulesManage.findNode(this.state.treeData, path[0]);
    return !course || course.status === 'DRAFT';
  }

  isModuleReleased(node, path) {
    if (node.type === 'MODULE') {
      return node.status === 'RELEASED';
    }

    const course = ModulesManage.findNode(this.state.treeData, path[0]);
    const module = ModulesManage.findNode(course.children, path[1]);

    return module !== undefined && module !== null && module.status === 'RELEASED';
  }

  canAddCourse(treeData) {
    const canAddCourse = !_.find(treeData, { status: 'DRAFT' });

    if ((canAddCourse && !this.state.canAddCourse) || (!canAddCourse && this.state.canAddCourse)) {
      this.setState(() => ({ canAddCourse }));
    }
  }

  render() {
    const canDrop = ({ prevPath, nextPath }) => prevPath.length === nextPath.length
      && prevPath[prevPath.length - 2] === nextPath[nextPath.length - 2];
    const canDrag = ({ node, path }) => node.type !== 'COURSE' && this.isEditable(node, path) && (node.type === 'MODULE' || !this.isModuleReleased(node, path));

    const onMoveNode = ({ node, prevPath }) => {
      if (node.type !== 'COURSE') {
        const nodeIndexPath = this.getNodeIndexPath(prevPath);

        if (node.type === 'MODULE') {
          this.setState(state => ({
            treeData: update(state.treeData, {
              [nodeIndexPath[0]]: { changed: { $set: true } },
            }),
          }));
        } else {
          this.setState(state => ({
            treeData: update(state.treeData, {
              [nodeIndexPath[0]]: {
                changed: { $set: true },
                children: {
                  [nodeIndexPath[1]]: { changed: { $set: true } },
                },
              },
            }),
          }));
        }

        this.reinitializeForm(nodeIndexPath);
      }
    };

    return (
      <div>
        <h1 className="page-header padding-bottom-xs margin-x-sm">{ hasAuthority(MANAGE_MODULES_AUTHORITY) ? 'Manage Modules' : 'Module List' }</h1>
        { hasAuthority(MANAGE_MODULES_AUTHORITY)
        && (
        <button
          type="button"
          className="btn btn-success margin-bottom-lg"
          onClick={this.addCourse}
          disabled={!this.state.canAddCourse}
        >
          <span className="fa fa-plus" />
          <span className="icon-text">Add Course</span>
        </button>
        )}
        <MotsConfirmModal
          showModal={this.state.showConfirmModal}
          modalParentId="page-wrapper"
          modalText="If you edit this module its progress will be removed for all CHW when this Course will be published, are you sure?"
          onConfirm={this.editModule}
          onHide={this.hideConfirmModal}
        />
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

                  this.props.resetLogoutCounter();
                },
                className: this.getNodeClassName(node),
                buttons: node.type === 'COURSE' || node.type === 'MODULE' || !this.isEditable(node, path) || this.isModuleReleased(node, path) ? [] : [
                  <button
                    type="button"
                    className="btn btn-danger"
                    onClick={(event) => {
                      this.removeNode(path, node);
                      event.stopPropagation();
                    }}
                  >
                    <span className="fa fa-trash" />
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
              this.state.selectedElement.path && (
              <ModuleForm
                onSubmit={this.saveNode}
                releaseCourse={this.releaseCourse}
                editModule={this.showConfirmModal}
                isEditable={this.isEditable(
                  this.state.selectedElement.node,
                  this.state.selectedElement.path,
                )}
                isDraftCourse={this.isDraftCourse(
                  this.state.selectedElement.node,
                  this.state.selectedElement.path,
                )}
                isModuleReleased={this.isModuleReleased(
                  this.state.selectedElement.node,
                  this.state.selectedElement.path,
                )}
                addModule={() => this.addModule(this.state.selectedElement.path)}
                addUnit={() => this.addUnit(this.state.selectedElement.path)}
                addMessage={() => this.addMessage(this.state.selectedElement.path)}
                addQuestion={() => this.addQuestion(this.state.selectedElement.path)}
              />
              )
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

export default connect(mapStateToProps, { initialize, resetLogoutCounter })(ModulesManage);

ModulesManage.propTypes = {
  history: PropTypes.shape({
    push: PropTypes.func,
  }).isRequired,
  resetLogoutCounter: PropTypes.func.isRequired,
  initialize: PropTypes.func.isRequired,
  formDirty: PropTypes.bool,
  formValues: PropTypes.shape({}),
};

ModulesManage.defaultProps = {
  formDirty: false,
  formValues: {},
};
