import React from 'react';
import PropTypes from 'prop-types';
import { View, Text } from 'react-native';

import styles from '../styles/listsStyles';
import commonStyles from '../styles/commonStyles';

const { lightThemeText } = commonStyles;

const ListItems = ({
  data, columns, canWrite, canAssign,
}) => (
  <View>
    {
      data.map(row => (
        <View
          key={row.id}
          style={styles.card}
        >
          {columns.map(column => (
            <View key={column.Header} style={styles.cardLine}>
              <Text style={[styles.bold, lightThemeText]}>{column.Header}:
                {!column.Cell &&
                <Text style={[styles.normal, lightThemeText]}>
                  {row[column.accessor]}
                </Text>
                }
              </Text>
              <View>
                { column.Cell &&
                column.Cell({ value: row[column.accessor], canWrite, canAssign })
                }
              </View>
            </View>
          ))}
        </View>
      ))
    }
  </View>
);

export default ListItems;

ListItems.propTypes = {
  data: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  columns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  canWrite: PropTypes.bool,
  canAssign: PropTypes.bool,
};

ListItems.defaultProps = {
  canWrite: false,
  canAssign: false,
};
