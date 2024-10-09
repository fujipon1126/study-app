module.exports = {
  branchPrefix: 'renovate/',
  dryRun: null,
  username: 'renovate-release',
  gitAuthor: 'Renovate Bot <bot@renovateapp.com>',
  platform: 'github',
  repositories: [
    'fujipon1126/study-app'
  ],
  labels: ["dependencies"],
  packageRules: [
    {
      // major,minor,patch以外のバージョンアップは無効
      matchUpdateTypes: [ 'pin', 'pinDigest', 'digest', 'lockFileMaintenance', 'rollback', 'bump'],
      enabled: false
    },
    {
      // minor,patchバージョンはautomergeする
      matchUpdateTypes: [ "minor","patch" ],
      automerge: true
    }
  ],
  extends: [
    "config:base",
    ":dependencyDashboard",
    ":prHourlyLimitNone",
    ":prConcurrentLimitNone"
  ]
};