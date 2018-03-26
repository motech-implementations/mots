import React from 'react';

import CsvUpload from './csv-upload';

const HealthWorkersUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/chw/upload"
      selectText="Select uploaded CHWs"
    />
  </div>
);

export default HealthWorkersUpload;
