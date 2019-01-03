import React from 'react';

import CsvUpload from './csv-upload';

const HealthWorkersUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/chw/upload"
      selectText="Select uploaded CHWs"
      uploadLabel="CSV File Upload for CHWs"
    />
  </div>
);

export default HealthWorkersUpload;
