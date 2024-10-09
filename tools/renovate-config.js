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
      "matchUpdateTypes": ["minor", "patch"],
      "schedule": ["after 10pm every weekday", "before 7am every weekday", "every weekend"], // 毎日22:00~翌7:00、週末
      "automerge": true,
    }
  ],
  extends: [
    "config:base",
    ":dependencyDashboard",
    ":prHourlyLimitNone",
    ":prConcurrentLimitNone"
  ]
};