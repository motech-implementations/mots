import React from 'react';
import PropTypes from 'prop-types';
import { View, Text } from 'react-native';

import styles from '../styles/listsStyles';
import commonStyles from '../styles/commonStyles';

const { lightThemeText } = commonStyles;

const ListItem = ({
  row, columns, canWrite, canAssign,
}) => (
  <View
    key={row.id}
    style={styles.card}
  >
    {columns.map(column => (
      <View key={column.displayName} style={styles.cardLine}>
        <Text
          style={[styles.bold, lightThemeText, { textAlign: 'center' }]}
        >{!column.hide && `${column.displayName}: `}
          {(!(column.Cell || column.TextCell) && !column.hide) &&
            <Text style={[styles.normal, lightThemeText]}>
              {row[column.name]}
            </Text>
          }
          { column.TextCell &&
            column.TextCell({ value: row[column.name] })
          }
        </Text>
        <View>
          { (column.Cell && !column.hide) &&
            column.Cell({
              value: row[column.name], canWrite, canAssign,
            })
          }
        </View>
      </View>
    ))}
  </View>
);

export default ListItem;

ListItem.propTypes = {
  row: PropTypes.shape({}).isRequired,
  columns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  canWrite: PropTypes.bool,
  canAssign: PropTypes.bool,
};

ListItem.defaultProps = {
  canWrite: false,
  canAssign: false,
};
