import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ReactDatePicker from 'react-datepicker';
import { parse, format, isDate } from 'date-fns';

import 'react-datepicker/dist/react-datepicker.css';

const DATE_FORMAT = 'yyyy-MM-dd';
const DATE_TIME_FORMAT = 'yyyy-MM-dd HH:mm';

// eslint-disable-next-line react/prefer-stateless-function
class CustomInput extends Component {
  render() {
    const {
      // eslint-disable-next-line react/prop-types
      value, onClick, onChange, placeholderText = '',
    } = this.props;

    return (
      <div className="input-group">
        <div className="input-group-prepend">
          <span className="input-group-text"><i className="fa fa-calendar" /></span>
        </div>
        <input
          placeholder={placeholderText}
          className="form-control"
          onChange={onChange}
          value={value}
          onClick={onClick}
          style={{ zIndex: 0 }}
        />
      </div>
    );
  }
}

const DatePicker = ({
  onChange, value, showTimeSelect, minDate, maxDate, startDate, endDate, placeholderText, ...props
}) => {
  const formatDate = date => (showTimeSelect ? format(date, DATE_TIME_FORMAT)
    : format(date, DATE_FORMAT));

  const parseDate = (val) => {
    if (!val) {
      return null;
    }

    if (isDate(val)) {
      return val;
    }

    return (showTimeSelect ? parse(val, DATE_TIME_FORMAT, new Date())
      : parse(val, DATE_FORMAT, new Date()));
  };

  const handleChange = (date) => {
    if (!date || typeof date === 'string') {
      onChange(date);
    } else {
      onChange(formatDate(date));
    }
  };

  return (
    <div className="modal-fields">
      <ReactDatePicker
        {...props}
        selected={parseDate(value)}
        showTimeSelect={showTimeSelect}
        dateFormat={showTimeSelect ? DATE_TIME_FORMAT : DATE_FORMAT}
        onChange={handleChange}
        timeFormat="HH:mm"
        timeIntervals={15}
        minDate={parseDate(minDate)}
        maxDate={parseDate(maxDate)}
        startDate={parseDate(startDate)}
        endDate={parseDate(endDate)}
        customInput={<CustomInput placeholderText={placeholderText} />}
      />
    </div>
  );
};

export default DatePicker;

DatePicker.propTypes = {
  onChange: PropTypes.func.isRequired,
  showTimeSelect: PropTypes.bool,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.shape({})]),
  minDate: PropTypes.oneOfType([PropTypes.string, PropTypes.shape({})]),
  maxDate: PropTypes.oneOfType([PropTypes.string, PropTypes.shape({})]),
  startDate: PropTypes.oneOfType([PropTypes.string, PropTypes.shape({})]),
  endDate: PropTypes.oneOfType([PropTypes.string, PropTypes.shape({})]),
  placeholderText: PropTypes.string,
};

DatePicker.defaultProps = {
  showTimeSelect: false,
  value: null,
  minDate: null,
  maxDate: null,
  startDate: null,
  endDate: null,
  placeholderText: null,
};
