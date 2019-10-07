#!/usr/bin/env bash

build_directory="$(pwd)"
website_directory="${HOME}/website"

prepare_git() {
  git config --local user.name "deja.consulting bot" &&\
  git config --local user.email "bot@deja.consulting"
}

clone_website() {
  echo 'Cloning website repository ...' &&\
  git clone "https://${DEPLOY_GITHUB_TOKEN}@github.com/deja-consulting/website.git" "${website_directory}" &&\
  echo 'Done cloning website repository.'
}

checkout_branch() {
  local branch_name="$1"
  echo "Checking out branch ${branch_name} ..." &&\
  cd "${website_directory}" &&\
  git checkout "$branch_name" &&\
  cd - &&\
  echo "Done checking out branch ${branch_name}."
}

copy_files_to_website() {
  echo 'Copying files to website repository ...' &&\
  cp "${build_directory}/target/Matthias-Deja-CV-Overview-de.pdf" "${website_directory}/static/docs/Matthias-Deja-CV-Overview-de.pdf" &&\
  echo "Done copying files to website repository."
}

push_changes() {
  echo 'Pushing changes ...' &&\
  cd "${website_directory}" &&\
  git add . &&\
  git commit -m "Latest CV v$BUILD_VERSION." &&\
  git push &&\
  cd - &&\
  echo 'Done pushing changes.'
}

update_staging() {
  echo 'Updating website staging version ...' &&\
  checkout_branch 'release/next' &&\
  copy_files_to_website &&\
  push_changes &&\
  echo 'Done updating website staging version.'
}

update_main() {
  echo 'Updating website main version ...' &&\
  checkout_branch 'release/latest' &&\
  copy_files_to_website &&\
  push_changes &&\
  echo 'Done updating website main version.'
}

delete_website_clone() {
  rm -rf "${website_directory}"
}

main() {
  case "${TRAVIS_BRANCH}" in
    'release/latest')
      echo 'Pushing changes to website for main release ...' &&\
      prepare_git && clone_website && update_staging && update_main && delete_website_clone &&\
      echo 'Done pushing changes for main release.'
      ;;
    'release/next')
      echo 'Pushing changes to website for staging release ...' &&\
      prepare_git && clone_website && update_staging && delete_website_clone &&\
      echo 'Done pushing changes for staging release.'
      ;;
    *)
      echo 'Not pushing changes to website, as this is neither a main nor staging release.'
      ;;
  esac
}

main
