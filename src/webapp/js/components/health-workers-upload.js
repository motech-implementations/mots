import React from 'react';

import CsvUpload from './csv-upload';

const HealthWorkersUpload = () => (
  <div>
    <CsvUpload uploadUrl="/api/chw/upload" />
  </div>
);

export default HealthWorkersUpload;
