import _ from 'lodash';
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm, formValueSelector, Field, FieldArray, FormSection } from 'redux-form';
import { connect } from 'react-redux';

export const MODULE_FORM_NAME = 'ModuleForm';

const QUESTION_FIELDS = {
  name: {
    label: 'Name',
    required: true,
  },
  content: {
    label: 'Content',
  },
  ivrId: {
    label: 'IVR Id',
  },
  ivrName: {
    label: 'IVR Name',
  },
  choices: {
    type: 'array',
    addLabel: 'Add Choice',
    fieldLabel: 'Choice',
    defaultValue: { isCorrect: false },
    fields: {
      ivrPressedKey: {
        label: 'IVR Pressed Key',
        required: true,
        attributes: {
          type: 'number',
        },
      },
      ivrName: {
        label: 'IVR Name',
      },
      isCorrect: {
        label: 'Is Correct',
        required: true,
        attributes: {
          type: 'checkbox',
          className: '',
        },
      },
      description: {
        label: 'Description',
      },
    },
  },
};

const MESSAGE_FIELDS = {
  name: {
    label: 'Name',
    required: true,
  },
  content: {
    label: 'Content',
  },
  ivrId: {
    label: 'IVR Id',
  },
  ivrName: {
    label: 'IVR Name',
  },
};

const UNIT_FIELDS = {
  name: {
    label: 'Name',
    required: true,
  },
  description: {
    label: 'Description',
  },
  ivrId: {
    label: 'IVR Id',
  },
  ivrName: {
    label: 'IVR Name',
  },
  allowReplay: {
    label: 'Allow Replay',
    required: true,
    attributes: {
      type: 'checkbox',
      className: '',
    },
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
  unitContinuationQuestion: {
    type: 'section',
    label: 'Unit Continuation Question',
    fields: QUESTION_FIELDS,
  },
};

const MODULE_FIELDS = {
  name: {
    label: 'Name',
    required: true,
  },
  description: {
    label: 'Description',
  },
  ivrId: {
    label: 'IVR Id',
  },
  ivrName: {
    label: 'IVR Name',
  },
  ivrGroup: {
    label: 'IVR Group',
  },
  moduleNumber: {
    label: 'Module Number',
    required: true,
    attributes: {
      type: 'number',
    },
  },
  buttons: [{
    label: 'Add Unit',
    callback: 'addUnit',
  }],
  startModuleQuestion: {
    type: 'section',
    label: 'Start Module Question',
    fields: QUESTION_FIELDS,
  },
};

class ModuleForm extends Component {
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
      if (!props.isEditable) {
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
        fieldConfig={fieldConfig}
        disabled={!props.isEditable}
      />
    );
  }

  static renderFieldArray = ({ fieldsConfig, properties, fields }) => (
    <div>
      <div className="text-center">
        { properties.isEditable &&
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
              { properties.isEditable &&
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
  )

  static renderFieldInput({
    fieldConfig, disabled, input, meta: { touched, error },
  }) {
    const { label, type, attributes } = fieldConfig;

    if (attributes && attributes.type === 'checkbox') {
      attributes.checked = input.value;
      attributes.onChange = (event) => {
        input.onChange(event.target.checked);
      };
    }

    const FieldType = type || 'input';
    const attr = {
      ...input,
      id: input.name,
      type: 'text',
      className: 'form-control',
      disabled,
      ...attributes,
    };

    const className = `form-group ${fieldConfig.required ? 'required' : ''} ${touched && error ? 'has-error' : ''}`;

    return (
      <div className={`padding-left-md padding-right-md ${className}`}>
        <div className="row">
          <label htmlFor={attr.id} className="col-md-4 control-label">{ label }</label>
          <div className="col-md-8">
            <FieldType {...attr} />
          </div>
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

  static getFields(nodeType) {
    switch (nodeType) {
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
          this.props.nodeType === 'MODULE' ?
            <form className="form-horizontal" onSubmit={handleSubmit(this.props.onSubmit)}>
              { _.map(ModuleForm.getFields(this.props.nodeType), (fieldConfig, fieldName) =>
                ModuleForm.renderField(fieldConfig, fieldName, fieldName, this.props)) }
              <div className="col-md-4" />
              { this.props.isEditable &&
              <button
                type="submit"
                disabled={!this.props.nodeChanged && (pristine || submitting)}
                className="btn btn-primary margin-bottom-md"
              >Save
              </button> }
              { this.props.isEditable &&
              <button
                type="button"
                disabled={this.props.isNew || this.props.nodeChanged || !pristine || submitting}
                className="btn btn-success margin-left-sm margin-bottom-md"
                onClick={this.props.releaseModule}
              >Publish
              </button> }
              { this.props.isEditable &&
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
    if (fieldConfig.type === 'section') {
      errors[fieldName] = validateFields(fieldConfig.fields, values[fieldName]);
    } else if (fieldConfig.type === 'array') {
      const array = values[fieldName];

      if (array) {
        errors[fieldName] = [];
        _.each(array, (value) => {
          errors[fieldName].push(validateFields(fieldConfig.fields, value));
        });
      }
    } else if (fieldConfig.required && (values[fieldName] === undefined || values[fieldName] === null || values[fieldName] === '')) {
      errors[fieldName] = 'This field is required';
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
    nodeChanged: selector(state, 'changed'),
    isNew: id === null || id === undefined,
  };
}

export default reduxForm({
  validate,
  form: MODULE_FORM_NAME,
})(connect(mapStateToProps)(ModuleForm));

ModuleForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  reset: PropTypes.func.isRequired,
  pristine: PropTypes.bool.isRequired,
  submitting: PropTypes.bool.isRequired,
  onSubmit: PropTypes.func.isRequired,
  releaseModule: PropTypes.func.isRequired,
  isEditable: PropTypes.bool.isRequired,
  nodeType: PropTypes.string,
  nodeChanged: PropTypes.bool,
  isNew: PropTypes.bool,
};

ModuleForm.defaultProps = {
  nodeType: null,
  nodeChanged: false,
  isNew: true,
};
