import React from 'react';
import PropTypes from 'prop-types';
import { View, Text } from 'react-native';

const styles = {
  card: {
    borderColor: '#ebebeb',
    borderRadius: 3,
    borderWidth: 1,
    marginHorizontal: 40,
    marginBottom: 20,
    padding: 10,
    backgroundColor: '#f8f8f8',
    justifyContent: 'center',
  },
  cardLine: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  bold: {
    fontWeight: 'bold',
  },
  normal: {
    fontWeight: 'normal',
  },
};

const ListItems = ({ data, columns }) => (
  <View>
    {
      data.map(row => (
        <View
          key={row.id}
          style={styles.card}
        >
          {columns.map(column => (
            <View key={column.Header} style={styles.cardLine}>
              <Text style={styles.bold}>{column.Header}:
                { !column.Cell && <Text style={styles.normal}> {row[column.accessor]}</Text> }
              </Text>
              <View>{ column.Cell && column.Cell({ value: row[column.accessor] }) }</View>
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
};
