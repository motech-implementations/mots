import React from 'react';
import PropTypes from 'prop-types';

const MobileTable = ({ data, columns }) => (
  <div>
    {
      data.map((row, index) => (
        <div
          className={`padding-md margin-sm mobile-chw-row ${
            index % 2 === 0 ? 'lighter-gray' : ''}`}
          key={row.chwId}
        >
          {columns.map(column => (
            <div className="padding-x-xxs" key={column.Header}>
              <strong>{column.Header}:</strong> {
                column.Cell && typeof column.Cell === 'function' ?
                  <div className="button-container margin-x-xs">
                    {column.Cell({ value: row[column.accessor] })}
                  </div> :
                  row[column.accessor]
                }
            </div>
          ))}
        </div>
      ))
    }
  </div>
);

export default MobileTable;

MobileTable.propTypes = {
  data: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  columns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
};
