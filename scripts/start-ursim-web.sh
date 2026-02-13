#!/usr/bin/env bash
set -euo pipefail

URL="http://localhost:6080/vnc.html?host=localhost&port=6080"
LOG_FILE="/tmp/ursim-entrypoint.log"
ACTION="${1:-run}"
ROBOT_MODEL="UR5"
RUN_USER="${SUDO_USER:-ur-dev}"

if [[ "${URSIM_WEB_AS_ROOT:-0}" != "1" && "$EUID" -ne 0 ]]; then
  exec sudo -E URSIM_WEB_AS_ROOT=1 "$0" "$@"
fi

usage() {
  echo "Usage: $0"
  echo "       $0 run"
  echo "       $0 start"
  echo "       $0 stop|status|logs [--follow]"
  echo "Examples:"
  echo "  $0"
  echo "  $0 run"
  echo "  $0 start"
  echo "  $0 status"
  echo "  $0 logs --follow"
  echo "  $0 stop"
}

stop_ursim() {
  pkill -f "runsvdir-ursim" >/dev/null 2>&1 || true
  pkill -f "runsv /etc/service/runsvdir-ursim" >/dev/null 2>&1 || true
  pkill -f "/opt/novnc/utils/novnc_proxy" >/dev/null 2>&1 || true
  pkill -f "websockify --web /opt/novnc 6080" >/dev/null 2>&1 || true
  pkill -f "x11vnc -bg -quiet -forever -shared -display :1" >/dev/null 2>&1 || true
  pkill -f "Xvfb :1" >/dev/null 2>&1 || true
  pkill -f "starturcontrol.sh" >/dev/null 2>&1 || true
  pkill -f "URControl" >/dev/null 2>&1 || true
  pkill -f "felix.jar" >/dev/null 2>&1 || true
}

status_ursim() {
  if pgrep -f "URControl" >/dev/null 2>&1; then
    echo "URSim status: running"
  else
    echo "URSim status: stopped"
  fi
  if pgrep -f "websockify --web /opt/novnc 6080" >/dev/null 2>&1; then
    echo "noVNC status: running (${URL})"
  else
    echo "noVNC status: stopped"
  fi
}

if [[ "$ACTION" == "-h" || "$ACTION" == "--help" ]]; then
  usage
  exit 0
fi

if [[ "$ACTION" == "logs" ]]; then
  if [[ "${2:-}" == "--follow" || "${3:-}" == "--follow" ]]; then
    tail -f "$LOG_FILE"
  else
    tail -n 200 "$LOG_FILE"
  fi
  exit 0
fi

if [[ "$ACTION" == "stop" ]]; then
  stop_ursim
  echo "URSim stopped."
  exit 0
fi

if [[ "$ACTION" == "status" ]]; then
  status_ursim
  exit 0
fi

if [[ "$ACTION" != "start" && "$ACTION" != "run" ]]; then
  if [[ "$ACTION" != "" ]]; then
    echo "Unknown argument '$ACTION' ignored. Using fixed model UR5."
  fi
  ACTION="run"
fi

echo "Restarting URSim web services for robot model ${ROBOT_MODEL} ..."
stop_ursim

chmod -R a+rwX /ursim/.urcaps /ursim/.urcontrol /ursim/programs.UR* /ursim/programs >/dev/null 2>&1 || true
chown -R "${RUN_USER}:${RUN_USER}" /ursim/.urcaps /ursim/.urcontrol /ursim/programs.UR* /ursim/programs >/dev/null 2>&1 || true
ln -sfn /ursim/programs /programs >/dev/null 2>&1 || true

echo "URSim Web UI: ${URL}"
if [[ -n "${BROWSER:-}" ]]; then
  "$BROWSER" "$URL" || true
fi

if [[ "$ACTION" == "start" ]]; then
  nohup env ROBOT_MODEL="${ROBOT_MODEL}" /entrypoint.sh polyscope_log >"${LOG_FILE}" 2>&1 &
  sleep 2
  echo "Logs: $0 logs --follow"
else
  trap 'echo; echo "Stopping URSim..."; stop_ursim; exit 0' INT TERM
  env ROBOT_MODEL="${ROBOT_MODEL}" /entrypoint.sh polyscope_log
fi
