import React from 'react';

import CsvUpload from './csv-upload';

const FacilityUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/facility/import"
      uploadLabel="CSV File Upload for Facilities"
    />
  </div>
);

export default FacilityUpload;
