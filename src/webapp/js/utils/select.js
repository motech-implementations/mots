import _ from 'lodash';
import React from 'react';
import ReactSelect from 'react-select';
import PropTypes from 'prop-types';

const Select = ({
  onChange, options, value, isMulti = false, ...props
}) => {
  const handleChange = (val) => {
    if (val && isMulti) {
      onChange(_.map(val, item => item.value));
    } else if (val && 'value' in val) {
      onChange(val.value);
    } else {
      onChange(val);
    }
  };

  const findValue = val => _.find(options, option => _.isEqual(option.value, val));

  const getValue = () => {
    if (!value) {
      return null;
    }

    if (isMulti) {
      return _.map(value, val => findValue(val));
    }

    return findValue(value);
  };

  return (
    <ReactSelect
      {...props}
      options={options}
      onChange={handleChange}
      value={getValue()}
      isMulti={isMulti}
    />
  );
};

export default Select;

Select.propTypes = {
  options: PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.string,
    PropTypes.shape({})])).isRequired,
  onChange: PropTypes.func.isRequired,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.shape({}),
    PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.string, PropTypes.shape({})]))]),
  isMulti: PropTypes.bool,
};

Select.defaultProps = {
  value: null,
  isMulti: false,
};
