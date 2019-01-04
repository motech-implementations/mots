import React from 'react';

import CsvUpload from './csv-upload';

const CommunityUpload = () => (
  <div>
    <CsvUpload
      uploadUrl="/api/community/import"
      uploadLabel="CSV File Upload for Communities"
    />
  </div>
);

export default CommunityUpload;
