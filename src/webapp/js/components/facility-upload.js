import React from 'react';

import CsvUpload from './csv-upload';

const FacilityUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/facility/import"
    />
  </div>
);

export default FacilityUpload;
