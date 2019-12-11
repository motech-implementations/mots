import React from 'react';

import CsvUpload from './csv-upload';

const VillageUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/village/import"
      uploadLabel="CSV File Upload for Villages"
    />
  </div>
);

export default VillageUpload;
