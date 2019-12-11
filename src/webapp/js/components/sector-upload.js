import React from 'react';

import CsvUpload from './csv-upload';

const SectorUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/sector/import"
      uploadLabel="CSV File Upload for Sectors"
    />
  </div>
);

export default SectorUpload;
