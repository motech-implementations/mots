import React from 'react';

import CsvUpload from './csv-upload';

const ChiefdomUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/chiefdom/import"
      uploadLabel="CSV File Upload for Chiefdoms"
    />
  </div>
);

export default ChiefdomUpload;
