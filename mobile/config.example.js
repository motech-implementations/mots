export default {
  backend: {
    instance: 'STAGING',
  },
  api: {
    LOCAL: 'http://10.0.2.2:8080',
    STAGING: 'http://mots.soldevelo.com',
    PROD: 'http://mots.soldevelo.com',
  },
  sentryConfig: {
    publicDSN: 'public_dsn',
  },
};
