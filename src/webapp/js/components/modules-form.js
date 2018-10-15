import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector, Field, FieldArray, FormSection } from 'redux-form';
import { connect } from 'react-redux';
import { Tooltip } from 'react-tippy';
import Select from 'react-select';

import 'react-tippy/dist/tippy.css';

import { resetLogoutCounter } from '../actions/index';
import { hasAuthority, MANAGE_MODULES_AUTHORITY } from '../utils/authorization';

export const MODULE_FORM_NAME = 'ModuleForm';

const QUESTION_FIELDS = {
  name: {
    label: 'Name',
    required: true,
    tooltip: 'Enter a meaningful  name of the question. <br /> This field is mandatory.',
  },
  content: {
    label: 'Content',
    tooltip: 'Enter a short description of the question. <br /> This field is optional.',
  },
  ivrId: {
    label: 'IVR Id',
    tooltip: 'Enter IVR Id of all questions in this module which you can find on Voto. <br /> This field is mandatory for publishing a course.',
  },
  ivrName: {
    label: 'IVR Name',
    tooltip: 'Enter the IVR Name of the question which you can find on Voto. <br /> This field is optional.',
  },
  questionType: {
    label: 'Question type',
    required: true,
    type: Select,
    tooltip: 'Whether this question belongs to pre- or post-test',
    getAttributes: (input) => {
      const options = [
        { value: 'Pre-test', label: 'Pre-test' },
        { value: 'Post-test', label: 'Post-test' },
      ];
      return {
        name: input.name,
        value: input.value,
        onChange: (value) => {
          input.onChange(value);
        },
        options,
        simpleValue: true,
      };
    },
  },
  choices: {
    type: 'array',
    addLabel: 'Add Choice',
    fieldLabel: 'Choice',
    defaultValue: { isCorrect: false },
    fields: {
      ivrName: {
        label: 'IVR Name',
        tooltip: 'Enter the IVR Name of the choice which you can find on Voto. <br /> This field is optional.',
      },
      isCorrect: {
        label: 'Is Correct',
        attributes: {
          type: 'checkbox',
          className: '',
        },
        tooltip: 'Check this field if this answer is the correct one out of the list of options.',
      },
      description: {
        label: 'Description',
        tooltip: 'Enter here a short description of the answer. <br /> This field is optional.',
      },
    },
  },
};

const MESSAGE_FIELDS = {
  name: {
    label: 'Name',
    required: true,
    tooltip: 'Enter a meaningful name of the message. <br /> This field is mandatory.',
  },
  content: {
    label: 'Content',
    tooltip: 'Enter a short description of the message.  <br /> This field is optional.',
  },
  ivrId: {
    label: 'IVR Id',
    tooltip: 'Enter IVR Id of all messages in this module which you can find on Voto. <br /> This field is mandatory for publishing a course.',
  },
  ivrName: {
    label: 'IVR Name',
    tooltip: 'Enter here the IVR Name of the message which you can find on Voto. <br /> This field is optional.',
  },
};

const UNIT_FIELDS = {
  name: {
    label: 'Name',
    required: true,
    tooltip: 'Enter a meaningful name of the unit. <br /> This field is mandatory.',
  },
  description: {
    label: 'Description',
    tooltip: 'Enter the description of the unit. <br /> This field is optional.',
  },
  ivrId: {
    label: 'IVR Id',
    tooltip: 'Enter IVR Id of all units in this module which you can find on Voto. <br /> This field is mandatory for publishing a course.',
  },
  ivrName: {
    label: 'IVR Name',
    tooltip: 'Enter the IVR Name of the unit which you can find on Voto. <br /> This field is optional.',
  },
  allowReplay: {
    label: 'Allow Replay',
    attributes: {
      type: 'checkbox',
      className: '',
    },
    tooltip: 'Check this field if you want to allow a replay of this unit during learning process. <br /> This field is optional.',
  },
  buttons: [
    {
      label: 'Add Message',
      callback: 'addMessage',
    },
    {
      label: 'Add Question',
      callback: 'addQuestion',
    },
  ],
};

const MODULE_FIELDS = {
  name: {
    label: 'Name',
    required: true,
    tooltip: 'Enter a meaningful name of the module. <br /> This field is mandatory.',
  },
  description: {
    label: 'Description',
    tooltip: 'Enter the description of the module. <br /> This field is optional.',
  },
  ivrId: {
    label: 'IVR Id',
    releasedEditable: true,
    tooltip: 'Enter IVR Id of the module which you can find on Voto. <br /> This field is mandatory for publishing a course.',
  },
  ivrName: {
    label: 'IVR Name',
    tooltip: 'Enter the IVR Name of the module which you can find on Voto. <br /> This field is optional.',
  },
  ivrGroup: {
    label: 'IVR Group',
    tooltip: 'Enter IVR Group of the module which you can find on Voto. <br /> This field is mandatory for publishing a course.',
  },
  buttons: [{
    label: 'Add Unit',
    callback: 'addUnit',
  }],
};

const COURSE_FIELDS = {
  name: {
    label: 'Name',
    tooltip: 'Enter a meaningful name of the course. <br /> This field is mandatory.',
  },
  description: {
    label: 'Description',
    tooltip: 'Enter the description of the course. <br /> This field is optional.',
  },
  ivrId: {
    label: 'IVR Id',
    tooltip: 'Enter IVR Id of the course which you can find on Voto. <br /> This field is mandatory for publishing a course.',
  },
  ivrName: {
    label: 'IVR Name',
    tooltip: 'Enter the IVR Name of the course which you can find on Voto. <br /> This field is optional.',
  },
  buttons: [{
    label: 'Add Module',
    callback: 'addModule',
  }],
};

class ModuleForm extends Component {
  static getFields(nodeType) {
    switch (nodeType) {
      case 'COURSE':
        return COURSE_FIELDS;
      case 'MODULE':
        return MODULE_FIELDS;
      case 'UNIT':
        return UNIT_FIELDS;
      case 'MESSAGE':
        return MESSAGE_FIELDS;
      case 'QUESTION':
        return QUESTION_FIELDS;
      default:
        return [];
    }
  }

  static renderSection({ fieldConfig, children }) {
    return (
      <div className="panel panel-default margin-bottom-lg">
        <div className="panel-heading">{fieldConfig.label}</div>
        <div className="panel-body">
          {children}
        </div>
      </div>
    );
  }

  static renderField(fieldConfig, fieldName, fieldKey, props) {
    if (fieldName === 'buttons') {
      if (!props.isEditable || props.isModuleReleased) {
        return null;
      }

      return (
        <div key={fieldKey}>
          <div className="col-md-4" />
          { _.map(fieldConfig, field => (
            <button
              type="button"
              key={field.label}
              className="btn btn-success margin-bottom-lg margin-right-sm"
              onClick={props[field.callback]}
            >
              <span className="glyphicon glyphicon-plus" />
              <span className="icon-text">{field.label}</span>
            </button>
          ))}
        </div>
      );
    }

    if (fieldConfig.type === 'array') {
      return (
        <FieldArray
          key={fieldKey}
          name={fieldName}
          component={ModuleForm.renderFieldArray}
          fieldsConfig={fieldConfig}
          properties={props}
        />
      );
    }

    if (fieldConfig.type === 'section') {
      return (
        <FormSection
          name={fieldName}
          key={fieldName}
          fieldConfig={fieldConfig}
          component={ModuleForm.renderSection}
        >
          { _.map(fieldConfig.fields, (config, name) =>
            ModuleForm.renderField(config, name, `${fieldName}.${name}`, props)) }
        </FormSection>
      );
    }

    return (
      <Field
        key={fieldKey}
        name={fieldName}
        component={ModuleForm.renderFieldInput}
        onFocus={props.resetLogoutCounter()}
        fieldConfig={fieldConfig}
        disabled={!props.isEditable || (props.isModuleReleased && !fieldConfig.releasedEditable)}
      />
    );
  }

  static renderFieldArray = ({ fieldsConfig, properties, fields }) => (
    <div>
      <div className="text-center">
        { properties.isEditable && !properties.isModuleReleased &&
        <button type="button" className="btn btn-success margin-bottom-lg" onClick={() => fields.push(fieldsConfig.defaultValue)}>
          <span className="glyphicon glyphicon-plus" />
          <span className="icon-text">{fieldsConfig.addLabel}</span>
        </button> }
      </div>
      <ul className="list-unstyled">
        {fields.map((field, index) => (
          // eslint-disable-next-line react/no-array-index-key
          <li key={index}>
            <div className="row">
              { properties.isEditable && !properties.isModuleReleased &&
              <button type="button" className="btn btn-danger pull-right margin-right-md" onClick={() => fields.remove(index)}>
                <span className="glyphicon glyphicon-trash" />
              </button> }
              <h4 className="margin-left-md">{`${fieldsConfig.fieldLabel} #${index + 1}`}</h4>
            </div>
            { _.map(fieldsConfig.fields, (fieldConfig, fieldName) =>
              ModuleForm.renderField(fieldConfig, `${field}.${fieldName}`, `${field}.${fieldName}`, properties)) }
          </li>))}
      </ul>
    </div>
  );

  static renderFieldInput({
    fieldConfig, disabled, input, meta: { touched, error },
  }) {
    const {
      label, type, attributes, getAttributes,
    } = fieldConfig;

    if (attributes && attributes.type === 'checkbox') {
      attributes.checked = input.value;
    }
    const dynamicAttr = getAttributes ? getAttributes(input) : { type: 'text', className: 'form-control', ...input };

    const FieldType = type || 'input';
    const attr = {
      id: input.name,
      disabled,
      ...attributes,
      ...dynamicAttr,
    };

    const className = `form-group ${fieldConfig.required ? 'required' : ''} ${touched && error ? 'has-error' : ''}`;

    return (
      <div className={`padding-left-md padding-right-md ${className}`}>
        <div className="row" >
          <Tooltip
            title={fieldConfig.tooltip}
            position="top"
            theme="transparent"
            animation="shift"
            arrow="true"
            followCursor="true"
            delay="150"
            duration="250"
            hideDelay="50"
            size="big"
            style={{ display: 'block' }}
          >
            <label htmlFor={attr.id} className="col-md-4 control-label">{ label }</label>
            <div className="col-md-8">
              <FieldType {...attr} />
            </div>
          </Tooltip>
        </div>
        <div className="row">
          <div className="col-md-4" />
          <div className="help-block col-md-8" style={{ float: 'left' }}>
            { touched ? error : '' }
          </div>
        </div>
      </div>
    );
  }

  render() {
    const {
      handleSubmit,
      reset,
      pristine,
      submitting,
    } = this.props;

    return (
      <div>
        {
          hasAuthority(MANAGE_MODULES_AUTHORITY) && (this.props.nodeType === 'MODULE' || this.props.nodeType === 'COURSE') ?
            <form className="form-horizontal" onSubmit={handleSubmit(this.props.onSubmit)}>
              { _.map(ModuleForm.getFields(this.props.nodeType), (fieldConfig, fieldName) =>
                ModuleForm.renderField(fieldConfig, fieldName, fieldName, this.props)) }
              <div className="col-md-4" />
              { this.props.isEditable && !this.props.isModuleReleased &&
              <button
                type="submit"
                disabled={!this.props.nodeChanged && (pristine || submitting)}
                className="btn btn-primary margin-bottom-md"
              >Save
              </button> }
              { this.props.nodeType === 'COURSE' && this.props.isEditable &&
              <button
                type="button"
                disabled={this.props.isNew || this.props.nodeChanged || !pristine || submitting}
                className="btn btn-success margin-left-sm margin-bottom-md"
                onClick={this.props.releaseCourse}
              >Publish
              </button> }
              { this.props.isDraftCourse && this.props.nodeType === 'MODULE' && this.props.isModuleReleased &&
              <button
                type="button"
                className="btn btn-primary margin-bottom-md"
                onClick={this.props.editModule}
              >Edit
              </button> }
              { this.props.isEditable && !this.props.isModuleReleased &&
              <button
                type="button"
                disabled={pristine || submitting}
                className="btn btn-danger margin-left-sm margin-bottom-md"
                onClick={reset}
              >Cancel
              </button> }
            </form>
            :
            <form className="form-horizontal" onSubmit={(event) => { event.preventDefault(); }}>
              { _.map(ModuleForm.getFields(this.props.nodeType), (fieldConfig, fieldName) =>
                ModuleForm.renderField(fieldConfig, fieldName, fieldName, this.props)) }
            </form>
        }
      </div>
    );
  }
}

function validateFields(fields, values) {
  const errors = {};

  _.each(fields, (fieldConfig, fieldName) => {
    const fieldValue = values[fieldName];

    if (fieldConfig.type === 'section') {
      errors[fieldName] = validateFields(fieldConfig.fields, fieldValue);
    } else if (fieldConfig.type === 'array') {
      if (fieldValue) {
        errors[fieldName] = [];
        _.each(fieldValue, (value) => {
          errors[fieldName].push(validateFields(fieldConfig.fields, value));
        });
      }
    } else if (fieldConfig.required && (fieldValue === undefined || fieldValue === null || fieldValue === '')) {
      errors[fieldName] = 'This field is required';
    } else if (fieldConfig.minVal !== null && fieldConfig.minVal !== undefined
      && fieldValue !== null && fieldValue !== undefined && fieldValue < fieldConfig.minVal) {
      errors[fieldName] = `This field value cannot be less than ${fieldConfig.minVal}`;
    }
  });

  return errors;
}

function validate(values) {
  const fields = ModuleForm.getFields(values.type);

  return validateFields(fields, values);
}

const selector = formValueSelector(MODULE_FORM_NAME);

function mapStateToProps(state) {
  const id = selector(state, 'id');
  return {
    nodeType: selector(state, 'type'),
    nodeStatus: selector(state, 'status'),
    nodeChanged: selector(state, 'changed'),
    isNew: id === null || id === undefined,
  };
}

export default reduxForm({
  validate,
  form: MODULE_FORM_NAME,
})(connect(mapStateToProps, { resetLogoutCounter })(ModuleForm));

ModuleForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  reset: PropTypes.func.isRequired,
  pristine: PropTypes.bool.isRequired,
  submitting: PropTypes.bool.isRequired,
  onSubmit: PropTypes.func.isRequired,
  releaseCourse: PropTypes.func.isRequired,
  editModule: PropTypes.func.isRequired,
  isEditable: PropTypes.bool.isRequired,
  isDraftCourse: PropTypes.bool.isRequired,
  isModuleReleased: PropTypes.bool.isRequired,
  nodeType: PropTypes.string,
  nodeStatus: PropTypes.string,
  nodeChanged: PropTypes.bool,
  isNew: PropTypes.bool,
  resetLogoutCounter: PropTypes.func.isRequired,
};

ModuleForm.defaultProps = {
  nodeType: null,
  nodeStatus: null,
  nodeChanged: false,
  isNew: true,
};
